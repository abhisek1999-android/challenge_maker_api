package com.abhisek.challenge_maker.challenge_maker.service;


import com.abhisek.challenge_maker.challenge_maker.dto.ChallengeDTO;
import com.abhisek.challenge_maker.challenge_maker.dto.ChallengeParticipantDTO;
import com.abhisek.challenge_maker.challenge_maker.model.ChallengeParticipant;
import com.abhisek.challenge_maker.challenge_maker.model.Challenges;
import com.abhisek.challenge_maker.challenge_maker.model.User;
import com.abhisek.challenge_maker.challenge_maker.repo.ChallengeParticipantRepository;
import com.abhisek.challenge_maker.challenge_maker.repo.ChallengeRepository;
import com.abhisek.challenge_maker.challenge_maker.repo.UserRepository;
import com.abhisek.challenge_maker.challenge_maker.repo.projections.ChallengeProjection;
import com.abhisek.challenge_maker.challenge_maker.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ChallengeParticipantRepository challengeParticipantRepository;

    public Challenges createChallenges(Challenges challenges){
        return challengeRepository.save(challenges);
    }

    public ChallengeParticipant createChallengeParticipant(ChallengeParticipantDTO dto){

        Challenges challenge = challengeRepository.findById(dto.getChallengeId())
                .orElseThrow(() -> new RuntimeException("Challenge not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChallengeParticipant participant = new ChallengeParticipant();
        participant.setChallenges(challenge);
        participant.setUser(user);
        participant.setStatus(dto.getStatus() != null ? dto.getStatus() : "pending"); // Default to 'pending' if status is not provided

        return challengeParticipantRepository.save(participant);
    }

    public List<Challenges> getParticipatingChallenges(int userId) {

        List<ChallengeParticipant> participating = challengeParticipantRepository.findByChallenges_MadeBy_Id(userId);

        if (participating.isEmpty()) {
            throw new RuntimeException("No challenge participants found for user with ID: " + userId);
        }
        return participating.stream()
                .map(ChallengeParticipant::getChallenges)
                .toList();
    }

    public List<ChallengeDTO> getAllChallenges() {

        UserInfoDetails userDetails = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int authenticatedUserId = userDetails.getId(); // Assuming there's a getId() method

       try {
           List<ChallengeProjection> projections = challengeRepository.findAllChallengesWithParticipationStatusNative(authenticatedUserId);

           return projections.stream().map(projection -> {
               ChallengeDTO dto = new ChallengeDTO();
               dto.setChallengeId(Utilities.hexToUUID(projection.getChallengeId()));
               dto.setTitle(projection.getTitle());
               dto.setDescription(projection.getDescription());
               dto.setInstruction(projection.getInstruction());
               dto.setVideoLink(projection.getVideoLink());
               dto.setIsParticipated(projection.getIsParticipated() == 1);
               dto.setCreatedAt(projection.getCreatedAt());
               dto.setUpdatedAt(projection.getUpdatedAt());

               // Fetch User
               User user = userRepository.findById(projection.getMadeById().longValue()).orElse(null);
               dto.setMadeBy(user);

               return dto;
           }).collect(Collectors.toList());
       }catch (Exception e) {
           System.out.println("Error fetching challenges: " + e.getMessage());
           throw new RuntimeException("Error fetching challenges: " + e.getMessage(), e);
       }
    }


/**
* The below function is not using sql query to fetch challenges and their participation status.
*/

//    public List<ChallengeDTO> getAllChallenges(){
//
//        List<Challenges> challenges = challengeRepository.findAll();
//        UserInfoDetails userDetails = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        int authenticatedUserId = userDetails.getId(); // Assuming there's a getId() method
//
//        Set<UUID> userParticipatedChallenges = challengeParticipantRepository.findByChallenges_MadeBy_Id(authenticatedUserId)
//                .stream()
//                .map(challengeParticipant -> challengeParticipant.getChallenges().getChallengeId())
//                .collect(Collectors.toSet());
//
//
//        return challenges.stream().map(
//                challenge ->{
//                    ChallengeDTO dto = new ChallengeDTO();
//                    dto.setChallengeId(challenge.getChallengeId());
//                    dto.setTitle(challenge.getTitle());
//                    dto.setDescription(challenge.getDescription());
//                    dto.setInstruction(challenge.getInstruction());
//                    dto.setVideoLink(challenge.getVideoLink());
//                    dto.setMadeBy(challenge.getMadeBy());
//                    dto.setCreatedAt(challenge.getCreatedAt());
//                    dto.setIsParticipated(userParticipatedChallenges.contains(challenge.getChallengeId()));
//                    return dto;
//                }
//        ).collect(Collectors.toList());
//    }



    /*THIS IMPLEMENTATION IS FOR PAGINATION PURPOSE*/
    public Page<ChallengeDTO> getAllChallenges(int page, int size) {
        // Create a Pageable object for pagination
        Pageable pageable = PageRequest.of(page, size);

        // Fetch a page of challenges instead of all challenges
        Page<Challenges> challengesPage = challengeRepository.findAll(pageable);

        UserInfoDetails userDetails = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int authenticatedUserId = userDetails.getId();

        // Get all challenge IDs the user is participating in
        Set<UUID> userParticipatedChallenges = challengeParticipantRepository.findByChallenges_MadeBy_Id(authenticatedUserId)
                .stream()
                .map(challengeParticipant -> challengeParticipant.getChallenges().getChallengeId())
                .collect(Collectors.toSet());

        // Convert the Page<Challenges> to Page<ChallengeDTO>
        return challengesPage.map(challenge -> {
            ChallengeDTO dto = new ChallengeDTO();
            dto.setChallengeId(challenge.getChallengeId());
            dto.setTitle(challenge.getTitle());
            dto.setDescription(challenge.getDescription());
            dto.setInstruction(challenge.getInstruction());
            dto.setVideoLink(challenge.getVideoLink());
            dto.setMadeBy(challenge.getMadeBy());
            dto.setCreatedAt(challenge.getCreatedAt());
            dto.setIsParticipated(userParticipatedChallenges.contains(challenge.getChallengeId()));
            return dto;
        });
    }

    public int updateChallengeParticipantStatus(ChallengeParticipantDTO dto, String videoUrl){

        UserInfoDetails userInfoDetails = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int authenticationUserId = userInfoDetails.getId();

        return challengeParticipantRepository.updateChallengeParticipant(
                authenticationUserId,dto.getChallengeId().toString(),videoUrl,dto.getStatus());
    }


    public List<Challenges> getAllChallengesByUserId(int userId){
        return challengeRepository.findByMadeBy_id(userId);
    }

    public Challenges getChallengesById(UUID id){
        return challengeRepository.findById(id).orElseThrow(() -> new RuntimeException("Challenge not found"));

    }
}
