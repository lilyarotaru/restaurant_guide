package ru.javaops.topjava2.util;

import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.DishTo;

public class DishUtil {

    public static Dish createFromTo(DishTo dishTo) {
        Restaurant restaurant = new Restaurant(dishTo.getRestaurantId(), null, null);
        return new Dish(null, dishTo.getName(), dishTo.getDishDate(), dishTo.getPrice(), restaurant);
    }

    public static Dish updateFromTo(Dish dish, DishTo dishTo) {
        dish.setName(dishTo.getName());
        dish.setPrice(dishTo.getPrice());
        dish.setDishDate(dishTo.getDishDate());
        return dish;
    }
}