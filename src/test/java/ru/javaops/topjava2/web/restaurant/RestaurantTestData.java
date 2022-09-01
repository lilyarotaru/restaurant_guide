package ru.javaops.topjava2.web.restaurant;

import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.web.MatcherFactory;

import java.util.List;

import static ru.javaops.topjava2.web.dish.DishTestData.*;

public class RestaurantTestData {

    public static final int RESTAURANT_ID_1 = 1;
    public static final int RESTAURANT_ID_2 = 2;
    public static final int RESTAURANT_ID_3 = 3;
    public static final int RESTAURANT_ID_4 = 4;
    public static final int NOT_FOUND = 100;

    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "dishes");

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT_ID_1, "Sakhalin", dishesOfRestaurant1);
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT_ID_2, "Terrassa", dishesOfRestaurant2);
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT_ID_3, "Mansarda", dishesOfRestaurant3);
    public static final Restaurant restaurant4 = new Restaurant(RESTAURANT_ID_4, "Katyusha", dishesOfRestaurant4);

    public static final List<Restaurant> restaurants = List.of(restaurant1, restaurant2, restaurant3, restaurant4);

    public static Restaurant getNew() {
        return new Restaurant(null, "New Restaurant", null);
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_ID_1, "Updated Sakhalin", null);
    }
}