package com.github.lilyarotaru.restaurantVoting.util;

import com.github.lilyarotaru.restaurantVoting.model.Dish;
import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import com.github.lilyarotaru.restaurantVoting.to.DishTo;

public class DishUtil {

    public static Dish createFromTo(DishTo dishTo, int restaurantId) {
        Restaurant restaurant = new Restaurant(restaurantId, null, null);
        return new Dish(null, dishTo.getName(), dishTo.getPrice(), restaurant);
    }

    public static Dish updateFromTo(Dish dish, DishTo dishTo) {
        dish.setName(dishTo.getName());
        dish.setPrice(dishTo.getPrice());
        return dish;
    }
}