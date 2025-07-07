package com.abhisek.challenge_maker.challenge_maker.controller;

import com.abhisek.challenge_maker.challenge_maker.ApiResponse;
import com.abhisek.challenge_maker.challenge_maker.dto.ChallengeDTO;
import com.abhisek.challenge_maker.challenge_maker.dto.ChallengeParticipantDTO;
import com.abhisek.challenge_maker.challenge_maker.model.ChallengeParticipant;
import com.abhisek.challenge_maker.challenge_maker.model.Challenges;
import com.abhisek.challenge_maker.challenge_maker.model.User;
import com.abhisek.challenge_maker.challenge_maker.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/challenges")
@Tag(name = "Challenges", description = "Endpoints for managing challenges")
public class ChallengeController {

    @Autowired
    public ChallengeService challengeService;

    @Value("${video.upload.dir:${user.home}/uploads/videos}")
    private String uploadDirectory;

    @PostMapping("/createChallenge")
    @Operation(summary = "Create a new challenge", description = "Creates a new challenge and saves it to the database")
    public ResponseEntity<ApiResponse<Challenges>> createChallenge(@RequestBody Challenges challenges) {
        try {
            Challenges savedChallenge = challengeService.createChallenges(challenges);
            ApiResponse<Challenges> response = new ApiResponse<>(1, 200, "Challenge created successfully", savedChallenge);
            return ResponseEntity.ok(response); // 200 OK
        } catch (IllegalArgumentException e) {
            ApiResponse<Challenges> response = new ApiResponse<>(0, 400, "Invalid input: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 400 Bad Request
        } catch (Exception e) {
            ApiResponse<Challenges> response = new ApiResponse<>(0, 500, "An error occurred while creating challenge: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 500
        }
    }


    @GetMapping("/getAllChallenges")
    @Operation(summary = "Get all challenges", description = "Retrieves a list of all challenges")
    public ResponseEntity<ApiResponse<List<ChallengeDTO>>> getAllChallenges(){
        List<ChallengeDTO> challenges = challengeService.getAllChallenges();

        if (challenges == null) {
            ApiResponse<List<ChallengeDTO>> response = new ApiResponse<>(0, 404, "User not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }else {
            ApiResponse<List<ChallengeDTO>> response = new ApiResponse<>(1, 200, "Challenges retrieved successfully ", challenges);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/getChallenges")
    public Page<ChallengeDTO> getChallenges(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return challengeService.getAllChallenges(page, size);
    }


    @PostMapping("/participateChallenge")
    @Operation(summary = "Participate in a challenge", description = "Allows a user to participate in a challenge")
    public ResponseEntity<ApiResponse<ChallengeParticipant>> participateOneChallenge(@RequestBody ChallengeParticipantDTO dto) {
        try {
            ChallengeParticipant savedChallengeParticipant = challengeService.createChallengeParticipant(dto);
            ApiResponse<ChallengeParticipant> response = new ApiResponse<>(1, 200, "Challenge participant created successfully", savedChallengeParticipant);
            return ResponseEntity.ok(response); // 200 OK
        } catch (IllegalArgumentException e) {
            ApiResponse<ChallengeParticipant> response = new ApiResponse<>(0, 400, "Invalid input: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 400 Bad Request
        } catch (SecurityException e) { // Example for forbidden access
            ApiResponse<ChallengeParticipant> response = new ApiResponse<>(0, 403, "Access forbidden: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response); // 403 Forbidden
        }

        catch (Exception e) {
            ApiResponse<ChallengeParticipant> response = new ApiResponse<>(0, 500, "An error occurred while creating challenge participant: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response); // 500
        }
    }

    @GetMapping("/getChallengesByUserId/{userId}")
    @Operation(summary = "Get challenges by user ID", description = "Retrieves challenges created by a specific user")
    public ResponseEntity<ApiResponse<List<Challenges>>> getChallengesByUserId(@PathVariable int userId){

        try{

            List<Challenges> challenges = challengeService.getAllChallengesByUserId(userId);
            if (challenges != null){
                ApiResponse<List<Challenges>> response = new ApiResponse<>(1,200,"Challenges retrieved successfully",challenges );
                return ResponseEntity.ok(response);
            }else {
                ApiResponse<List<Challenges>> response = new ApiResponse<>(1,200,"Challenges retrieved successfully",null);
                return ResponseEntity.ok(response);
            }

        }catch (IllegalArgumentException e){
            ApiResponse<List<Challenges>> response = new ApiResponse<>(0,400,"An error occurred while retrieving challenge: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        catch (SecurityException e) { // Example for forbidden access
            ApiResponse<List<Challenges>> response = new ApiResponse<>(0, 403, "Access forbidden: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response); // 403 Forbidden
        }
        catch (Exception e){
            ApiResponse<List<Challenges>> response = new ApiResponse<>(0,500,"An error occurred while retrieving challenge: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/getChallengeById/{id}")
    @Operation(summary = "Get challenge by ID", description = "Retrieves a specific challenge by its ID")
    public ResponseEntity<ApiResponse<Challenges>> getChallengeById(@PathVariable UUID id){

        try{
            Challenges challenge = challengeService.getChallengesById(id);
            ApiResponse<Challenges> response = new ApiResponse<>(1, 200, "Challenge retrieved successfully", challenge);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            ApiResponse<Challenges> response = new ApiResponse<>(0, 400, "Invalid input: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
        catch (SecurityException e) { // Example for forbidden access
            ApiResponse<Challenges> response = new ApiResponse<>(0, 403, "Access forbidden: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response); // 403 Forbidden
        }
        catch (Exception e){
            ApiResponse<Challenges> response = new ApiResponse<>(0, 500, "An error occurred while retrieving challenge: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/markChallengeAsComplete")
    public ResponseEntity<ApiResponse<Integer>> completeChallenge(@RequestPart("videoFile")MultipartFile file, @RequestPart("data") ChallengeParticipantDTO dto) {
        try {

            File directory = new File(uploadDirectory);
            if (!directory.exists()){
                directory.mkdirs();
            }

            String fileName = UUID.randomUUID().toString() +"_"+file.getOriginalFilename();

            Path filePath = Paths.get(uploadDirectory, fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            int result = challengeService.updateChallengeParticipantStatus(dto, String.valueOf(filePath));
            if (result > 0) {
                ApiResponse<Integer> response = new ApiResponse<>(1, 200, "Challenge marked as complete successfully", result);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Integer> response = new ApiResponse<>(0, 404, "Challenge participant not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (IllegalArgumentException e) {
            ApiResponse<Integer> response = new ApiResponse<>(0, 400, "Invalid input: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<Integer> response = new ApiResponse<>(0, 500, "An error occurred while marking challenge as complete: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }


    }


    @GetMapping("/getParticipatingChallenges/{userId}")
    public ResponseEntity<ApiResponse<List<Challenges>>> getParticipatingChallenges(@PathVariable int userId) {
        try {
            List<Challenges> challenges = challengeService.getParticipatingChallenges(userId);
            if (challenges != null && !challenges.isEmpty()) {
                ApiResponse<List<Challenges>> response = new ApiResponse<>(1, 200, "Participating challenges retrieved successfully", challenges);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<List<Challenges>> response = new ApiResponse<>(0, 404, "No participating challenges found for the user", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (IllegalArgumentException e) {
            ApiResponse<List<Challenges>> response = new ApiResponse<>(0, 400, "Invalid input: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<List<Challenges>> response = new ApiResponse<>(0, 500, "An error occurred while retrieving participating challenges: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
