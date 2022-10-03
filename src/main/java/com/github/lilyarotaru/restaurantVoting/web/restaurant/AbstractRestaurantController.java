package com.github.lilyarotaru.restaurantVoting.web.restaurant;

import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import com.github.lilyarotaru.restaurantVoting.repository.RestaurantRepository;
import com.github.lilyarotaru.restaurantVoting.util.validation.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
public class AbstractRestaurantController {

    @Autowired
    protected RestaurantRepository repository;

    public Restaurant getWithDishes(@PathVariable int id) {
        log.info("get {} with dishes", id);
        return ValidationUtil.checkNotFound(repository.getWithDishes(id), id);
    }

    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll();
    }
}
