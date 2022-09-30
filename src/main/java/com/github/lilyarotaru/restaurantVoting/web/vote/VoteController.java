package com.github.lilyarotaru.restaurantVoting.web.vote;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
public class VoteController {
    public static final LocalTime DEADLINE = LocalTime.of(11, 0);
}
