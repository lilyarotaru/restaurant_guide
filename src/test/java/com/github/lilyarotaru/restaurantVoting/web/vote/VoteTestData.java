package com.github.lilyarotaru.restaurantVoting.web.vote;

import com.github.lilyarotaru.restaurantVoting.model.Restaurant;
import com.github.lilyarotaru.restaurantVoting.model.Vote;
import com.github.lilyarotaru.restaurantVoting.web.MatcherFactory;
import com.github.lilyarotaru.restaurantVoting.web.restaurant.RestaurantTestData;
import com.github.lilyarotaru.restaurantVoting.web.user.UserTestData;

import java.time.LocalDate;

public class VoteTestData {

    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user");

    public static final Vote userTodayVote = new Vote(1, LocalDate.now(), UserTestData.user,
            new Restaurant(RestaurantTestData.RESTAURANT_ID_2, null, null));

}