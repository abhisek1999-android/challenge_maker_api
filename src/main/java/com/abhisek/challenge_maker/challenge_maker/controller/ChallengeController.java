package com.abhisek.challenge_maker.challenge_maker.controller;

import com.abhisek.challenge_maker.challenge_maker.model.Challenges;
import com.abhisek.challenge_maker.challenge_maker.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    @Autowired
    public ChallengeService challengeService;

    @PostMapping
    public ResponseEntity<?> createChallenge(@RequestBody Challenges challenges) {
        try {
            Challenges savedChallenge = challengeService.createChallenges(challenges);
            return ResponseEntity.ok(savedChallenge); // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage()); // 400 Bad Request
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating challenge: " + e.getMessage()); // 500
        }
    }


    @GetMapping
    public List<Challenges> getAllChallenges(){
        return challengeService.getAllChallenges();
    }

    @GetMapping("/{id}")
    public Challenges getChallengeById(@PathVariable UUID id){

        return challengeService.getChallengesById(id);
    }

}
