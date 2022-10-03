package com.github.lilyarotaru.restaurantVoting.web.restaurant;

import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import com.github.lilyarotaru.restaurantVoting.repository.RestaurantRepository;
import com.github.lilyarotaru.restaurantVoting.util.JsonUtil;
import com.github.lilyarotaru.restaurantVoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.lilyarotaru.restaurantVoting.web.restaurant.RestaurantTestData.*;
import static com.github.lilyarotaru.restaurantVoting.web.user.UserTestData.ADMIN_MAIL;
import static com.github.lilyarotaru.restaurantVoting.web.user.UserTestData.USER_MAIL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestaurantController.REST_URL + "/";

    @Autowired
    private RestaurantRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_ID_1))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.NOT_FOUND))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.NOT_FOUND))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getWithDishes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_ID_1 + "/with-dishes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER_WITH_DISHES.contentJson(restaurant1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getWithDishesNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND + "/with-dishes"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT_ID_1))
                .andExpect(status().isNoContent())
                .andDo(print());
        assertFalse(repository.findById(RESTAURANT_ID_1).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RestaurantTestData.NOT_FOUND))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurants));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = RestaurantTestData.getNew();
        ResultActions result = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isCreated())
                .andDo(print());
        Restaurant created = RESTAURANT_MATCHER.readFromJson(result);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(repository.getById(newId), newRestaurant);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Restaurant newRestaurant = RestaurantTestData.getNew();
        newRestaurant.setName("");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = RestaurantTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent())
                .andDo(print());
        RESTAURANT_MATCHER.assertMatch(repository.getById(RESTAURANT_ID_1), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Restaurant updated = RestaurantTestData.getUpdated();
        updated.setName("");
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }
}