package com.abhisek.challenge_maker.challenge_maker.controller;


import com.abhisek.challenge_maker.challenge_maker.ApiResponse;
import com.abhisek.challenge_maker.challenge_maker.model.AuthRequest;
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

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

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
    public ResponseEntity<ApiResponse<String>> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(authRequest.getUserName());
                ApiResponse<String> response = new ApiResponse<>(1, 200, "Token generated successfully", token);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<String> response = new ApiResponse<>(0, 401, "Invalid user request!", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (BadCredentialsException e) {
            ApiResponse<String> response = new ApiResponse<>(0, 401, "Invalid username or password", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (UsernameNotFoundException e) {
            ApiResponse<String> response = new ApiResponse<>(0, 404, "User not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(0, 500, "An error occurred during authentication: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
