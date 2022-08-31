package ru.javaops.topjava2.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.util.validation.ValidationUtil;
import ru.javaops.topjava2.web.AuthUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "restaurants")
public class RestaurantController {
    static final String REST_URL = "api/restaurants";

    @Autowired
    protected RestaurantRepository repository;

    @Autowired
    private VoteRepository voteRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.getByIdWithDishes(id));
    }

    @GetMapping
    @Cacheable
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll();
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Vote vote(@PathVariable int id, @AuthenticationPrincipal AuthUser authUser) {
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDay = LocalDate.now();
        Restaurant refRestaurant = repository.getById(id);
        Vote vote = new Vote(currentDay, authUser.getUser(), refRestaurant);
        try {
            return voteRepository.save(vote);
        } catch (DataIntegrityViolationException e) {
            ValidationUtil.checkVotingTime(currentTime);
            voteRepository.update(id, currentDay, authUser.id());
            return vote;    //returns with null id
        }
    }
}
