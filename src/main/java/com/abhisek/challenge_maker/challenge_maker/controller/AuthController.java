package com.abhisek.challenge_maker.challenge_maker.controller;


import com.abhisek.challenge_maker.challenge_maker.ApiResponse;
import com.abhisek.challenge_maker.challenge_maker.model.AuthRequest;
import com.abhisek.challenge_maker.challenge_maker.model.RefreshToken;
import com.abhisek.challenge_maker.challenge_maker.model.User;
import com.abhisek.challenge_maker.challenge_maker.security.JwtService;
import com.abhisek.challenge_maker.challenge_maker.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserInfoService userDetailsService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody User userInfo) {
        return service.addUser(userInfo);
    }

    @PostMapping("/user/getUserProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ApiResponse<User>> userProfile(@RequestParam String userName) {

        User user = service.getUserInfo(userName);
        if (user == null) {
            ApiResponse<User> response = new ApiResponse<>(0, 404, "User not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }else {
            ApiResponse<User> response = new ApiResponse<>(1, 200, "User found", user);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public ResponseEntity<ApiResponse<Map<String,String>>> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                String username = authRequest.getUserName();
                String accessToken = jwtService.generateToken(authRequest.getUserName());
                String refreshTokenStr = jwtService.generateRefreshToken(authRequest.getUserName());

                User user = userDetailsService.getUserInfo(username);
                if (user == null) {
                    ApiResponse<Map<String,String>> response = new ApiResponse<>(0, 404, "User not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }

                RefreshToken refreshToken = new RefreshToken();
                refreshToken.setToken(refreshTokenStr);
                refreshToken.setUser(user);
                refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));

                userDetailsService.saveRefreshToken(refreshToken);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshTokenStr); // or new one if you rotate

                ApiResponse<Map<String,String>> response = new ApiResponse<>(1, 200, "Token generated successfully", tokens);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Map<String,String>> response = new ApiResponse<>(0, 401, "Invalid user request!", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (BadCredentialsException e) {
            ApiResponse<Map<String,String>> response = new ApiResponse<>(0, 401, "Invalid username or password", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (UsernameNotFoundException e) {
            ApiResponse<Map<String,String>> response = new ApiResponse<>(0, 404, "User not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<Map<String,String>> response = new ApiResponse<>(0, 500, "An error occurred during authentication: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String,String> request){

        String refreshToken = request.get("refreshToken");
        try {
            // 1. Check if token exists in DB
            Optional<RefreshToken> optionalToken = Optional.ofNullable(userDetailsService.getRefreshToken(refreshToken));

            if (optionalToken.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found");
            }

            RefreshToken tokenRecord = optionalToken.get();

            // 2. Check if expired
            if (tokenRecord.getExpiryDate().isBefore(Instant.now())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
            }

            // 3. Extract username from token
            String username = jwtService.extractUsername(refreshToken);

            // 4. Generate new access token
            String newAccessToken = jwtService.generateToken(username);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            tokens.put("refreshToken", refreshToken); // or rotate here if you want

            return ResponseEntity.ok(tokens);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

}
