package com.abhisek.challenge_maker.challenge_maker.repo;

import com.abhisek.challenge_maker.challenge_maker.model.RefreshToken;
import com.abhisek.challenge_maker.challenge_maker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);

}
