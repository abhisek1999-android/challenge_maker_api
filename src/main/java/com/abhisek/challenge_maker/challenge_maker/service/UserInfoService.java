package com.abhisek.challenge_maker.challenge_maker.service;


import com.abhisek.challenge_maker.challenge_maker.model.RefreshToken;
import com.abhisek.challenge_maker.challenge_maker.model.User;
import com.abhisek.challenge_maker.challenge_maker.repo.RefreshTokenRepository;
import com.abhisek.challenge_maker.challenge_maker.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> userDetail = repository.findByUserName(userName); // Assuming 'email' is used as username

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));
    }

    public String addUser(User userInfo) {
        // Encode password before saving the user
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully";
    }



    public User getUserInfo(String userName) {
        return repository.findByUserName(userName).orElse(null); // Replace with actual user retrieval logic
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public String saveRefreshToken(RefreshToken refreshToken){
        refreshTokenRepository.save(refreshToken);
        return "Refresh token saved successfully";
    }

    public RefreshToken getRefreshToken(String refreshToken){
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }


}
