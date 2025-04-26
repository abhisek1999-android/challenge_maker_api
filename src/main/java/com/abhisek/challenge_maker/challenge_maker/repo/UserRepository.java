package com.abhisek.challenge_maker.challenge_maker.repo;

import com.abhisek.challenge_maker.challenge_maker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
}
