package ru.javaops.topjava2.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.util.JsonUtil;
import ru.javaops.topjava2.web.AbstractControllerTest;
import ru.javaops.topjava2.web.GlobalExceptionHandler;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.web.dish.DishTestData.DISH_MATCHER;
import static ru.javaops.topjava2.web.dish.DishTestData.dishesOfRestaurant1;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.*;
import static ru.javaops.topjava2.web.user.UserTestData.ADMIN_MAIL;
import static ru.javaops.topjava2.web.user.UserTestData.USER_MAIL;

class RestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantController.REST_URL + "/";

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        ResultActions result = perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_ID_1))
                .andExpect(status().isOk())
                .andDo(print());
        Restaurant restaurant = RESTAURANT_MATCHER.readFromJson(result);
        RESTAURANT_MATCHER.assertMatch(restaurant, restaurant1);
        DISH_MATCHER.assertMatch(restaurant.getDishes(), dishesOfRestaurant1);
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_ID_1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurants));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @Transactional(propagation = Propagation.NEVER)
        //populate db with user's vote for restaurant 2
    void changeVote() throws Exception {
        ResultActions result = perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT_ID_1 + "/votes"));
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
        MvcResult result = perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT_ID_1 + "/votes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        Vote vote = JsonUtil.readValue(result.getResponse().getContentAsString(), Vote.class);
        assertEquals(RESTAURANT_ID_1, (int) vote.getRestaurantId());
        assertEquals(LocalDate.now(), vote.getVoteDate());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void voteNotExistedRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + NOT_FOUND + "/votes"))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }
}