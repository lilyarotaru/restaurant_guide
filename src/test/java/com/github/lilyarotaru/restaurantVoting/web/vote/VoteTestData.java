package com.github.lilyarotaru.restaurantVoting.web.vote;

import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import com.github.lilyarotaru.restaurantVoting.model.Vote;
import com.github.lilyarotaru.restaurantVoting.web.MatcherFactory;
import com.github.lilyarotaru.restaurantVoting.web.user.UserTestData;

import java.time.LocalDate;

import static com.github.lilyarotaru.restaurantVoting.web.restaurant.RestaurantTestData.restaurant1;
import static com.github.lilyarotaru.restaurantVoting.web.restaurant.RestaurantTestData.restaurant2;

public class VoteTestData {

    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user");

    public static final Vote userTodayVote = new Vote(1, LocalDate.now(), UserTestData.user,
            new Restaurant(restaurant2.getId(), restaurant2.getName(), null));

    public static Vote newAdminVoteForRestaurant1() {
        return new Vote(null, LocalDate.now(), UserTestData.admin,
                new Restaurant(restaurant1.getId(), restaurant1.getName(), null));
    }
}