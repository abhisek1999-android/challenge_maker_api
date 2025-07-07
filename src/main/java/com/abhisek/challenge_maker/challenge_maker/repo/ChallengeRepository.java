package com.abhisek.challenge_maker.challenge_maker.repo;

import com.abhisek.challenge_maker.challenge_maker.model.Challenges;
import com.abhisek.challenge_maker.challenge_maker.repo.projections.ChallengeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenges, UUID> {
    List<Challenges> findByMadeBy_id(int userId);

    @Query(value = "SELECT HEX(c.challenge_id) as challengeId, c.made_by_id, c.title, c.description, " +
            "c.instruction, c.video_link, " +
            "CASE WHEN cp.participant_id IS NOT NULL THEN 1 ELSE 0 END as is_participated, " +
            "c.created_at, c.updated_at " +
            "FROM challenges c " +
            "LEFT JOIN challenge_participants cp ON c.challenge_id = cp.challenge_id " +
            "AND cp.user_id = :userId",
            nativeQuery = true)
    List<ChallengeProjection> findAllChallengesWithParticipationStatusNative(@Param("userId") int userId);

//    @Query(value = "SELECT c.challenge_id, c.made_by_id, c.title, c.description, " +
//            "c.instruction, c.video_link, " +
//            "CASE WHEN cp.participant_id IS NOT NULL THEN 1 ELSE 0 END as is_participated, " +
//            "c.created_at, c.updated_at " +
//            "FROM challenges c " +
//            "LEFT JOIN challenge_participants cp ON c.challenge_id = cp.challenge_id " +
//            "AND cp.user_id = :userId",
//            nativeQuery = true)
//    List<Object[]> findAllChallengesWithParticipationStatusNative(@Param("userId") int userId);
}
