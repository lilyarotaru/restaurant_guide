package com.github.lilyarotaru.restaurantVoting.web.vote;

import com.github.lilyarotaru.restaurantVoting.model.Vote;
import com.github.lilyarotaru.restaurantVoting.web.AbstractControllerTest;
import com.github.lilyarotaru.restaurantVoting.web.restaurant.RestaurantTestData;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.*;

import static com.github.lilyarotaru.restaurantVoting.web.GlobalExceptionHandler.EXCEPTION_CHANGING_VOTE;
import static com.github.lilyarotaru.restaurantVoting.web.GlobalExceptionHandler.EXCEPTION_TWICE_VOTE;
import static com.github.lilyarotaru.restaurantVoting.web.restaurant.RestaurantTestData.RESTAURANT_ID_1;
import static com.github.lilyarotaru.restaurantVoting.web.user.UserTestData.ADMIN_MAIL;
import static com.github.lilyarotaru.restaurantVoting.web.user.UserTestData.USER_MAIL;
import static com.github.lilyarotaru.restaurantVoting.web.vote.VoteTestData.VOTE_MATCHER;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ServletUriComponentsBuilder.newInstance()
            .path(VoteController.REST_URL).buildAndExpand(RESTAURANT_ID_1).toString();

    @Test
    @WithUserDetails(value = USER_MAIL)
    @Transactional(propagation = Propagation.NEVER)
        //db was populated with today's user's vote to restaurant 2
    void changeVoteAfterDeadLine() throws Exception {
        LocalDateTime afterDeadline = LocalDate.now().atTime(15, 0);
        Instant instant = afterDeadline.atZone(ZoneId.systemDefault()).toInstant();
        Clock fixedClock = Clock.fixed(instant, ZoneId.systemDefault());

        // Mock static method Clock.systemDefaultZone() which used in LocalTime.now(), to return fixed clock
        // https://asolntsev.github.io/en/2020/07/11/mockito-static-methods/

        try (MockedStatic<Clock> mockedStatic = mockStatic(Clock.class)) {
            mockedStatic.when(Clock::systemDefaultZone).thenReturn(fixedClock);
            perform(MockMvcRequestBuilders.put(REST_URL))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString(EXCEPTION_CHANGING_VOTE)))
                    .andDo(print());
        }
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void changeVoteBeforeDeadLine() throws Exception {

        LocalDateTime beforeDeadline = LocalDate.now().atTime(7, 0);
        Instant instant = beforeDeadline.atZone(ZoneId.systemDefault()).toInstant();
        Clock fixedClock = Clock.fixed(instant, ZoneId.systemDefault());

        try (MockedStatic<Clock> mockedStatic = mockStatic(Clock.class)) {
            mockedStatic.when(Clock::systemDefaultZone).thenReturn(fixedClock);
            ResultActions result = perform(MockMvcRequestBuilders.put(REST_URL))
                    .andExpect(status().isOk())
                    .andDo(print());
            Vote changed = VoteTestData.usersChangedVoteRofRestaurant1();
            Vote actual = VOTE_MATCHER.readFromJson(result);
            VOTE_MATCHER.assertMatch(actual, changed);
        }
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void vote() throws Exception {
        ResultActions result = perform(MockMvcRequestBuilders.post(REST_URL))
                .andExpect(status().isCreated())
                .andDo(print());
        Vote created = VOTE_MATCHER.readFromJson(result);
        Vote newVote = VoteTestData.newAdminVoteForRestaurant1();
        newVote.setId(created.getId());
        VOTE_MATCHER.assertMatch(created, newVote);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void voteTwice() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_TWICE_VOTE)))
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