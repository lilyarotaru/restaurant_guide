package com.github.lilyarotaru.restaurantVoting.web.restaurant;

import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import com.github.lilyarotaru.restaurantVoting.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
public class AbstractRestaurantController {

    @Autowired
    protected RestaurantRepository repository;

    @Cacheable(cacheNames = "restaurantsWithMenu", key = "#id")
    public ResponseEntity<Restaurant> getWithDishes(@PathVariable int id) {
        log.info("get {} with dishes", id);
        return ResponseEntity.of(repository.getWithDishes(id));
    }

    @Cacheable(cacheNames = "restaurants")
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll();
    }
}
