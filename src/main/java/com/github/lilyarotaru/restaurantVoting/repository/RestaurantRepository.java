package com.github.lilyarotaru.restaurantVoting.repository;

import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @EntityGraph(attributePaths = "dishes")
    @Query("SELECT r FROM Restaurant r LEFT JOIN r.dishes d ON d.dishDate=current_date where r.id=?1")
    Optional<Restaurant> getWithDishes(int id);

    @EntityGraph(attributePaths = "dishes")
    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN  r.dishes d on d.dishDate=current_date")
    List<Restaurant> findAllWithDishes();
}