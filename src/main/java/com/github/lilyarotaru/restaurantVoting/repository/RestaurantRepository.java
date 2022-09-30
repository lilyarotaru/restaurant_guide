package com.github.lilyarotaru.restaurantVoting.repository;

import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r LEFT JOIN fetch r.dishes d where r.id=?1 AND d.dishDate=current_date ")
    Optional<Restaurant> getWithDishes(int id);
}