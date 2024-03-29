package com.github.lilyarotaru.restaurantVoting.web.dish;

import com.github.lilyarotaru.restaurantVoting.model.Dish;
import com.github.lilyarotaru.restaurantVoting.repository.DishRepository;
import com.github.lilyarotaru.restaurantVoting.repository.RestaurantRepository;
import com.github.lilyarotaru.restaurantVoting.to.DishTo;
import com.github.lilyarotaru.restaurantVoting.util.DishUtil;
import com.github.lilyarotaru.restaurantVoting.util.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
@CacheConfig(cacheNames = "restaurants")
public class AdminDishController {
    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/dishes";

    private final DishRepository repository;

    private final RestaurantRepository restaurantRepository;

    @GetMapping
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("getAll for restaurant with id={}", restaurantId);
        List<Dish> result = repository.findByRestaurantId(restaurantId);
        if (result.isEmpty()) {
            //if restaurant with {restaurantId} doesn't exist - return 404 with body to inform client that request has error
            ValidationUtil.checkNotFound(restaurantRepository.findById(restaurantId), restaurantId);
        }
        return result;
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get {}", id);
        return ValidationUtil.checkNotFound(repository.findByIdAndRestaurantId(id, restaurantId), id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(allEntries = true)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody DishTo dishTo, @PathVariable int restaurantId) {
        log.info("create {}", dishTo);
        ValidationUtil.checkNew(dishTo);
        Dish created = repository.save(DishUtil.createFromTo(dishTo, restaurantId));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id, restaurantId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Transactional      //to avoid "select" and "update" while save()
    public void update(@Valid @RequestBody DishTo dishTo, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update {} with id={}", dishTo, id);
        ValidationUtil.assureIdConsistent(dishTo, id);
        Dish dish = ValidationUtil.checkNotFound(repository.findByIdAndRestaurantId(id, restaurantId), id);
        repository.save(DishUtil.updateFromTo(dish, dishTo));
    }
}