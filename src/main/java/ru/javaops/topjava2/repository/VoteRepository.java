package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.model.Vote;

import java.time.LocalDate;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    Vote findByVoteDateAndUserId(LocalDate voteDate, int user_id);

    @Transactional
    @Modifying
    @Query("UPDATE Vote SET restaurant.id=?1 WHERE id=?2")
    void update(int restaurantId, int id);
}
