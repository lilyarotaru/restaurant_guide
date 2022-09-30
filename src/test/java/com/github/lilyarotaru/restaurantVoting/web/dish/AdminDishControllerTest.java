package com.github.lilyarotaru.restaurantVoting.web.dish;

import com.github.lilyarotaru.restaurantVoting.model.Dish;
import com.github.lilyarotaru.restaurantVoting.repository.DishRepository;
import com.github.lilyarotaru.restaurantVoting.web.AbstractControllerTest;
import com.github.lilyarotaru.restaurantVoting.web.restaurant.RestaurantTestData;
import com.github.lilyarotaru.restaurantVoting.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.github.lilyarotaru.restaurantVoting.util.JsonUtil.writeValue;
import static com.github.lilyarotaru.restaurantVoting.web.dish.DishTestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminDishControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ServletUriComponentsBuilder.newInstance()
            .path(AdminDishController.REST_URL + "/").buildAndExpand(RestaurantTestData.RESTAURANT_ID_1).toString();

    @Autowired
    private DishRepository repository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dishesOfRestaurant1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_ID_1))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish_1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotExistedRestaurant() throws Exception {
        String restUrlNotExistedRestaurant = AdminDishController.REST_URL.replace("{restaurantId}", String.valueOf(NOT_FOUND));
        perform(MockMvcRequestBuilders.get(restUrlNotExistedRestaurant))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform((MockMvcRequestBuilders.get(REST_URL + DISH_ID_1)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getForbidden() throws Exception {
        perform((MockMvcRequestBuilders.get(REST_URL + DISH_ID_1)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = getNew();
        ResultActions result = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isCreated());
        Dish created = DISH_MATCHER.readFromJson(result);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(repository.getById(newId), newDish);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createInvalid() throws Exception {
        Dish newDish = getNew();
        newDish.setPrice(-10);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH_ID_1))
                .andExpect(status().isNoContent())
                .andDo(print());
        assertFalse(repository.findById(DISH_ID_1).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isNoContent())
                .andDo(print());
        DISH_MATCHER.assertMatch(repository.getById(DISH_ID_1), updated);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Dish updated = getUpdated();
        updated.setName("");
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Dish duplicate = new Dish(null, dish_1.getName(), dish_1.getDishDate(), 999, dish_1.getRestaurant());
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(duplicate)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Dish duplicate = new Dish(DISH_ID_1, dish_2.getName(), dish_2.getDishDate(), 999, dish_2.getRestaurant());
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(duplicate)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }
}