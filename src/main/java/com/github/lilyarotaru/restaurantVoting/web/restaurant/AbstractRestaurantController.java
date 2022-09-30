package com.github.lilyarotaru.restaurantVoting.web.restaurant;

import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import com.github.lilyarotaru.restaurantVoting.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Slf4j
public class AbstractRestaurantController {

    @Autowired
    protected RestaurantRepository repository;

    @Cacheable(cacheNames = "restaurantsWithMenu", key = "#id")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get {}", id);
        Optional<Restaurant> restaurant = repository.getWithDishes(id);
        return ResponseEntity.of(restaurant);
    }

    @Cacheable(cacheNames = "restaurants")
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll();
    }
}
