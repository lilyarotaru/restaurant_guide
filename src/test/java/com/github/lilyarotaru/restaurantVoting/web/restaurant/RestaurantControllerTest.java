package com.github.lilyarotaru.restaurantVoting.web.restaurant;

import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import com.github.lilyarotaru.restaurantVoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.lilyarotaru.restaurantVoting.web.dish.DishTestData.DISH_MATCHER;
import static com.github.lilyarotaru.restaurantVoting.web.dish.DishTestData.dishesOfRestaurant1;
import static com.github.lilyarotaru.restaurantVoting.web.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantController.REST_URL + "/";

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        ResultActions result = perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT_ID_1))
                .andExpect(status().isOk())
                .andDo(print());
        Restaurant restaurant = RestaurantTestData.RESTAURANT_MATCHER.readFromJson(result);
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(restaurant, RestaurantTestData.restaurant1);
        DISH_MATCHER.assertMatch(restaurant.getDishes(), dishesOfRestaurant1);
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT_ID_1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(RestaurantTestData.restaurants));
    }


}