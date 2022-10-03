package com.github.lilyarotaru.restaurantVoting.web.vote;

import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import com.github.lilyarotaru.restaurantVoting.model.Vote;
import com.github.lilyarotaru.restaurantVoting.web.MatcherFactory;

import java.time.LocalDate;

import static com.github.lilyarotaru.restaurantVoting.web.restaurant.RestaurantTestData.restaurant1;
import static com.github.lilyarotaru.restaurantVoting.web.restaurant.RestaurantTestData.restaurant2;
import static com.github.lilyarotaru.restaurantVoting.web.user.UserTestData.admin;
import static com.github.lilyarotaru.restaurantVoting.web.user.UserTestData.user;

public class VoteTestData {

    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user");

    public static final Vote userTodayVote = new Vote(1, LocalDate.now(), user,
            new Restaurant(restaurant2.getId(), restaurant2.getName(), null));

    public static Vote newAdminVoteForRestaurant1() {
        return new Vote(null, LocalDate.now(), admin,
                new Restaurant(restaurant1.getId(), restaurant1.getName(), null));
    }

    public static Vote usersChangedVoteRofRestaurant1() {
        return new Vote(userTodayVote.getId(), userTodayVote.getVoteDate(),
                user, new Restaurant(restaurant1.getId(), restaurant1.getName(), null));
    }
}