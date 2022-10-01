package com.github.lilyarotaru.restaurantVoting.web.vote;

import com.github.lilyarotaru.restaurantVoting.error.IllegalRequestDataException;
import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import com.github.lilyarotaru.restaurantVoting.model.Vote;
import com.github.lilyarotaru.restaurantVoting.repository.RestaurantRepository;
import com.github.lilyarotaru.restaurantVoting.repository.VoteRepository;
import com.github.lilyarotaru.restaurantVoting.util.validation.ValidationUtil;
import com.github.lilyarotaru.restaurantVoting.web.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static com.github.lilyarotaru.restaurantVoting.web.GlobalExceptionHandler.EXCEPTION_TWICE_VOTE;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
@Transactional
public class VoteController {
    public static final LocalTime DEADLINE = LocalTime.of(11, 0);

    static final String REST_URL = "/api/restaurants/{restaurantId}/vote";

    private final VoteRepository repository;
    private final RestaurantRepository restaurantRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vote createNewVote(@PathVariable int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("user(id={}) votes for restaurant(id={})", authUser.id(), restaurantId);
        LocalDate currentDay = LocalDate.now();
        Restaurant restaurant = ValidationUtil.checkNotFound(restaurantRepository.findById(restaurantId), restaurantId);
        try {
            return repository.save(new Vote(currentDay, authUser.getUser(), restaurant));
        } catch (DataIntegrityViolationException e) {
            //exception if user try to create second vote in one day
            throw new IllegalRequestDataException(EXCEPTION_TWICE_VOTE);
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Vote> changeVote(@PathVariable int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("user(id={}) change vote to restaurant(id={})", authUser.id(), restaurantId);
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDay = LocalDate.now();
        Restaurant restaurant = ValidationUtil.checkNotFound(restaurantRepository.findById(restaurantId), restaurantId);
        Optional<Vote> existed = repository.findByVoteDateAndUserId(currentDay, authUser.id());
        if (existed.isPresent()) {
            ValidationUtil.checkVotingTime(currentTime);
            existed.get().setRestaurant(restaurant);
            return ResponseEntity.ok(existed.get());
        } else {
            //if user doesn't vote today and send PUT-request,we should create a new vote and send 201 http code
            Vote created = repository.save(new Vote(currentDay, authUser.getUser(), restaurant));
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        }
    }
}
