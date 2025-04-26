package com.abhisek.challenge_maker.challenge_maker.repo;

import com.abhisek.challenge_maker.challenge_maker.model.Challenges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenges, UUID> {
}
