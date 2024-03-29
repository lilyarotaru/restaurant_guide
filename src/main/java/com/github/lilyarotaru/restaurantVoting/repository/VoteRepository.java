package com.github.lilyarotaru.restaurantVoting.repository;

import com.github.lilyarotaru.restaurantVoting.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.voteDate=?1 AND v.user.id=?2")
    Optional<Vote> findByVoteDateAndUserId(LocalDate voteDate, int userId);

    @EntityGraph(attributePaths = "restaurant")
    @Query("SELECT v FROM Vote v WHERE v.voteDate=current_date AND v.user.id=?1")
    Optional<Vote> findByUserId(int userId);
}
