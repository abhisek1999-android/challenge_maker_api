package com.abhisek.challenge_maker.challenge_maker.service;


import com.abhisek.challenge_maker.challenge_maker.model.Challenges;
import com.abhisek.challenge_maker.challenge_maker.repo.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    public Challenges createChallenges(Challenges challenges){
        return challengeRepository.save(challenges);
    }

    public List<Challenges> getAllChallenges(){
        return challengeRepository.findAll();
    }

    public Challenges getChallengesById(UUID id){
        return challengeRepository.findById(id).orElseThrow(() -> new RuntimeException("Challenge not found"));

    }
}
