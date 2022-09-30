package com.github.lilyarotaru.restaurantVoting.web.restaurant;

import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import com.github.lilyarotaru.restaurantVoting.model.Vote;
import com.github.lilyarotaru.restaurantVoting.util.validation.ValidationUtil;
import com.github.lilyarotaru.restaurantVoting.web.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.github.lilyarotaru.restaurantVoting.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantController extends AbstractRestaurantController {
    static final String REST_URL = "/api/restaurants";

    @Autowired
    private VoteRepository voteRepository;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @GetMapping
    public List<Restaurant> getAll() {
        return super.getAll();
    }

    @PostMapping("/{restaurantId}/votes")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public Vote vote(@PathVariable int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("user(id={}) votes for restaurant(id={})", authUser.id(), restaurantId);
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDay = LocalDate.now();
        Optional<Vote> existed = voteRepository.findByVoteDateAndUserId(currentDay, authUser.id());
        if (existed.isPresent()) {
            ValidationUtil.checkVotingTime(currentTime);
            existed.get().setRestaurantId(restaurantId);
            return existed.get();
        } else {
            return voteRepository.save(new Vote(currentDay, authUser.getUser(), restaurantId));
        }
    }
}