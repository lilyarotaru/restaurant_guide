package ru.javaops.topjava2.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.RestaurantRepository;

import java.time.LocalDate;
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
        restaurant.ifPresent(value -> {
            if (!value.getDishes().isEmpty()) {
                List<Dish> todayDishes = value.getDishes().stream()
                        .takeWhile(dish -> dish.getDishDate().equals(LocalDate.now())).toList();
                value.setDishes(todayDishes);
            }
        });
        return ResponseEntity.of(restaurant);
    }

    @Cacheable(cacheNames = "restaurants")
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll();
    }
}
