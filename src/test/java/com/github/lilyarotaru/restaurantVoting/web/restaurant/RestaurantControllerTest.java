package com.github.lilyarotaru.restaurantVoting.web.restaurant;

import com.github.lilyarotaru.restaurantVoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.lilyarotaru.restaurantVoting.web.restaurant.RestaurantTestData.*;
import static com.github.lilyarotaru.restaurantVoting.web.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantController.REST_URL + "/";

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_ID_1))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(RESTAURANT_MATCHER_WITH_DISHES.contentJson(restaurant1));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_ID_1))
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
                .andExpect(RESTAURANT_MATCHER_WITH_DISHES.contentJson(restaurants));
    }
}