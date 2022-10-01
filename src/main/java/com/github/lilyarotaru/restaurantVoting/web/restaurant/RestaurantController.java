package com.github.lilyarotaru.restaurantVoting.web.restaurant;

import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantController extends AbstractRestaurantController {
    static final String REST_URL = "/api/restaurants";

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getWithDishes(@PathVariable int id) {
        return super.getWithDishes(id);
    }

    @Override
    @GetMapping
    public List<Restaurant> getAll() {
        return super.getAll();
    }
}