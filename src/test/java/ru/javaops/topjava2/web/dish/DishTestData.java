package ru.javaops.topjava2.web.dish;

import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.*;

public class DishTestData {

    private static final LocalDate TODAY = LocalDate.now();
    private static final int DISH_ID_START = 0;
    public static final int DISH_ID_1 = 1;
    public static final int DISH_ID_2 = 2;
    public static final int DISH_ID_3 = 3;
    public static final int NOT_FOUND = 100;

    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurant");

    public static final Dish dish_1 = new Dish(DISH_ID_1, "Sakhalin fish soup", TODAY, 900, RESTAURANT_ID_1);
    public static final Dish dish_2 = new Dish(DISH_ID_2, "Grilled dorado", TODAY, 1050, RESTAURANT_ID_1);
    public static final Dish dish_3 = new Dish(DISH_ID_3, "Crab with avocado, cucumbers and tomatoes", TODAY, 900, RESTAURANT_ID_1);
    public static final Dish dish_4 = new Dish(DISH_ID_START + 4, "Mango and grilled tuna salad", TODAY, 800, RESTAURANT_ID_2);
    public static final Dish dish_5 = new Dish(DISH_ID_START + 5, "Andalusian gazpacho", TODAY, 900, RESTAURANT_ID_2);
    public static final Dish dish_6 = new Dish(DISH_ID_START + 6, "Duck breast with yams", TODAY, 1300, RESTAURANT_ID_2);
    public static final Dish dish_7 = new Dish(DISH_ID_START + 7, "Greek salad", TODAY, 700, RESTAURANT_ID_3);
    public static final Dish dish_8 = new Dish(DISH_ID_START + 8, "Ribeye", TODAY, 1500, RESTAURANT_ID_3);
    public static final Dish dish_9 = new Dish(DISH_ID_START + 9, "Tom Yum with seafood", TODAY, 1000, RESTAURANT_ID_3);
    public static final Dish dish_10 = new Dish(DISH_ID_START + 10, "Okroshka with kvass", TODAY, 800, RESTAURANT_ID_4);
    public static final Dish dish_11 = new Dish(DISH_ID_START + 11, "Russian salad", TODAY, 750, RESTAURANT_ID_4);
    public static final Dish dish_12 = new Dish(DISH_ID_START + 12, "Chicken with potatoes", TODAY, 950, RESTAURANT_ID_4);

    public static final List<Dish> dishesOfRestaurant1 = List.of(dish_1, dish_2, dish_3);
    public static final List<Dish> dishesOfRestaurant2 = List.of(dish_4, dish_5, dish_6);
    public static final List<Dish> dishesOfRestaurant3 = List.of(dish_7, dish_8, dish_9);
    public static final List<Dish> dishesOfRestaurant4 = List.of(dish_10, dish_11, dish_12);

    public static final List<Dish> allDishes = List.of(dish_1, dish_2, dish_3, dish_4, dish_5, dish_6, dish_7, dish_8, dish_9, dish_10, dish_11, dish_12);

    public static Dish getNew() {
        return new Dish(null, "New dish", TODAY, 999, RESTAURANT_ID_1);
    }

    public static Dish getUpdated() {
        return new Dish(DISH_ID_1, "Updated dish", TODAY, 888, RESTAURANT_ID_1);
    }
}
