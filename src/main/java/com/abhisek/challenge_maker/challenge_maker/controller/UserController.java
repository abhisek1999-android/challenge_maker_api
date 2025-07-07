package com.abhisek.challenge_maker.challenge_maker.controller;

import com.abhisek.challenge_maker.challenge_maker.model.User;
import com.abhisek.challenge_maker.challenge_maker.repo.UserRepository;
import com.abhisek.challenge_maker.challenge_maker.service.UserInfoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserInfoService userService;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

//
//    @GetMapping("/{user_id}")
//    public User getEmployeeById(@PathVariable Long user_id) {
//        return userService.getUserById(user_id);
//    }
//
//    @PostMapping
//    public User createEmployee(@Valid @RequestBody User user) {
//        return userService.createUser(user);
//    }
}
