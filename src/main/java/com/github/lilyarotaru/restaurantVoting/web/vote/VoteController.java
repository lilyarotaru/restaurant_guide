package com.github.lilyarotaru.restaurantVoting.web.vote;

import com.github.lilyarotaru.restaurantVoting.error.AppException;
import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import com.github.lilyarotaru.restaurantVoting.model.Vote;
import com.github.lilyarotaru.restaurantVoting.repository.RestaurantRepository;
import com.github.lilyarotaru.restaurantVoting.repository.VoteRepository;
import com.github.lilyarotaru.restaurantVoting.util.validation.ValidationUtil;
import com.github.lilyarotaru.restaurantVoting.web.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

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
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Vote> vote(@PathVariable int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("user(id={}) votes for restaurant(id={})", authUser.id(), restaurantId);
        LocalDate currentDay = LocalDate.now();
        Restaurant restaurant = ValidationUtil.checkNotFound(restaurantRepository.findById(restaurantId), restaurantId);
        try {
            Vote created = repository.save(new Vote(currentDay, authUser.getUser(), restaurant));
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL)//TODO change url to uri that user can see his vote
                    .buildAndExpand(restaurantId).toUri();
            return ResponseEntity.created(uriOfNewResource).body(created);
        } catch (DataIntegrityViolationException e) {
            //exception if user try to create second vote in one day
            throw new AppException(HttpStatus.UNPROCESSABLE_ENTITY, "You have already voted today", ErrorAttributeOptions.of(MESSAGE));
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Vote> changeVote(@PathVariable int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("user(id={}) votes for restaurant(id={})", authUser.id(), restaurantId);
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDay = LocalDate.now();
        Restaurant restaurant = ValidationUtil.checkNotFound(restaurantRepository.findById(restaurantId), restaurantId);
        Optional<Vote> existed = repository.findByVoteDateAndUserId(currentDay, authUser.id());
        if (existed.isPresent()) {
            ValidationUtil.checkVotingTime(currentTime);
            existed.get().setRestaurant(restaurant);
            return ResponseEntity.of(existed);
        } else {
            return vote(restaurantId, authUser);
        }
    }
}
