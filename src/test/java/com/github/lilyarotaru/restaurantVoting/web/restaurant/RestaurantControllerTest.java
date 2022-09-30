package com.github.lilyarotaru.restaurantVoting.web.restaurant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import com.github.lilyarotaru.restaurantVoting.model.Vote;
import com.github.lilyarotaru.restaurantVoting.util.JsonUtil;
import com.github.lilyarotaru.restaurantVoting.web.AbstractControllerTest;
import com.github.lilyarotaru.restaurantVoting.web.GlobalExceptionHandler;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.lilyarotaru.restaurantVoting.web.dish.DishTestData.DISH_MATCHER;
import static com.github.lilyarotaru.restaurantVoting.web.dish.DishTestData.dishesOfRestaurant1;
import static com.github.lilyarotaru.restaurantVoting.web.user.UserTestData.ADMIN_MAIL;
import static com.github.lilyarotaru.restaurantVoting.web.user.UserTestData.USER_MAIL;

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

    @Test
    @WithUserDetails(value = USER_MAIL)
    @Transactional(propagation = Propagation.NEVER)
        //populate db with user's vote for restaurant 2
    void changeVote() throws Exception {
        ResultActions result = perform(MockMvcRequestBuilders.post(REST_URL + RestaurantTestData.RESTAURANT_ID_1 + "/votes"));
        if (LocalTime.now().isAfter(Vote.DEADLINE)) {
            result.andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_CHANGING_VOTE)))
                    .andDo(print());
        } else {
            result.andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void vote() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.post(REST_URL + RestaurantTestData.RESTAURANT_ID_1 + "/votes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        Vote vote = JsonUtil.readValue(result.getResponse().getContentAsString(), Vote.class);
        Assertions.assertEquals(RestaurantTestData.RESTAURANT_ID_1, (int) vote.getRestaurantId());
        assertEquals(LocalDate.now(), vote.getVoteDate());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void voteNotExistedRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + RestaurantTestData.NOT_FOUND + "/votes"))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }
}