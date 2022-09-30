package com.github.lilyarotaru.restaurantVoting.web.vote;

import com.github.lilyarotaru.restaurantVoting.model.Vote;
import com.github.lilyarotaru.restaurantVoting.util.JsonUtil;
import com.github.lilyarotaru.restaurantVoting.web.AbstractControllerTest;
import com.github.lilyarotaru.restaurantVoting.web.GlobalExceptionHandler;
import com.github.lilyarotaru.restaurantVoting.web.restaurant.RestaurantTestData;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.github.lilyarotaru.restaurantVoting.web.user.UserTestData.ADMIN_MAIL;
import static com.github.lilyarotaru.restaurantVoting.web.user.UserTestData.USER_MAIL;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ServletUriComponentsBuilder.newInstance()
            .path(VoteController.REST_URL).buildAndExpand(RestaurantTestData.RESTAURANT_ID_1).toString();

    @Test
    @WithUserDetails(value = USER_MAIL)
    @Transactional(propagation = Propagation.NEVER)
        //populate db with user's vote for restaurant 2
    void changeVoteAfterDeadLine() throws Exception {
        ResultActions result = perform(MockMvcRequestBuilders.put(REST_URL));
        if (LocalTime.now().isAfter(VoteController.DEADLINE)) {
            result.andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_CHANGING_VOTE)))
                    .andDo(print());
        } else {
            result.andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @Transactional(propagation = Propagation.NEVER)
        //populate db with user's vote for restaurant 2
    void changeVoteBeforeDeadLine() throws Exception {
        ResultActions result = perform(MockMvcRequestBuilders.put(REST_URL));
        if (LocalTime.now().isAfter(VoteController.DEADLINE)) {
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
        MvcResult result = perform(MockMvcRequestBuilders.post(REST_URL))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        Vote vote = JsonUtil.readValue(result.getResponse().getContentAsString(), Vote.class);
        assertEquals(RestaurantTestData.RESTAURANT_ID_1, vote.getRestaurant().id());
        assertEquals(LocalDate.now(), vote.getVoteDate());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void voteTwice() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void voteNotExistedRestaurant() throws Exception {
        final String REST_URL_NOT_FOUND = ServletUriComponentsBuilder.newInstance()
                .path(VoteController.REST_URL).buildAndExpand(RestaurantTestData.NOT_FOUND).toString();
        perform(MockMvcRequestBuilders.post(REST_URL_NOT_FOUND))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}