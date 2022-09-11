package ru.javaops.topjava2.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.util.validation.ValidationUtil;
import ru.javaops.topjava2.web.AuthUser;

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
    public Vote vote(@PathVariable int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("user(id={}) votes for restaurant(id={})", authUser.id(), restaurantId);
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDay = LocalDate.now();
        Vote vote = new Vote(currentDay, authUser.getUser(), restaurantId);
        try {
            return voteRepository.saveAndFlush(vote);
        } catch (DataIntegrityViolationException e) {
            ValidationUtil.checkVotingTime(currentTime);
            Optional<Vote> existed = voteRepository.findByVoteDateAndUserId(currentDay, authUser.id());
            existed.ifPresent(value -> {
                value.setRestaurantId(restaurantId);
                voteRepository.update(restaurantId, value.id());
            });
            return existed.orElseThrow(() -> e);
        }
    }
}