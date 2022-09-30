package com.github.lilyarotaru.restaurantVoting.repository;

import com.github.lilyarotaru.restaurantVoting.model.Dish;
import com.github.lilyarotaru.restaurantVoting.util.validation.ValidationUtil;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=?1")
    //use query because spring data jpa make unnecessary left outer join restaurant, even though fetch type is Lazy
    List<Dish> findByRestaurantId(int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.id=?1 AND d.restaurant.id=?2")
    Optional<Dish> findByIdAndRestaurantId(int id, int restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish WHERE id=?1 AND restaurant.id=?2")
    int delete(int id, int restaurantId);

    default void deleteExisted(int id, int restaurantId) {
        ValidationUtil.checkModification(delete(id, restaurantId), id);
    }
}