package ru.javaops.topjava2.web.dish;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.repository.DishRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.to.DishTo;
import ru.javaops.topjava2.util.DishUtil;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.javaops.topjava2.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminDishController {
    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/dishes";

    @Autowired
    private DishRepository repository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("getAll for restaurant with id={}", restaurantId);
        List<Dish> result = repository.findByRestaurantId(restaurantId);
        if (result.isEmpty()) {
            //if restaurant with {restaurantId} doesn't exist - return 404 with body to inform client that request has error
            restaurantRepository.findById(restaurantId).orElseThrow(notFoundWithId(restaurantId));
        }
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findByIdAndRestaurantId(id, restaurantId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(cacheNames = "restaurantsWithMenu", key = "#restaurantId")
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody DishTo dishTo, @PathVariable int restaurantId) {
        log.info("create {}", dishTo);
        checkNew(dishTo);
        Dish created = repository.save(DishUtil.createFromTo(dishTo, restaurantId));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(cacheNames = "restaurantsWithMenu", key = "#restaurantId")
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id, restaurantId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(cacheNames = "restaurantsWithMenu", key = "#restaurantId")
    @Transactional      //to avoid "select" and "update" while save()
    public void update(@Valid @RequestBody DishTo dishTo, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update {} with id={}", dishTo, id);
        assureIdConsistent(dishTo, id);
        Dish dish = repository.findByIdAndRestaurantId(id, restaurantId)
                .orElseThrow(notFoundWithId(id));
        repository.save(DishUtil.updateFromTo(dish, dishTo));
    }
}