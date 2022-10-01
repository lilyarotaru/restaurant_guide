package com.github.lilyarotaru.restaurantVoting.web.user;

import com.github.lilyarotaru.restaurantVoting.model.User;
import com.github.lilyarotaru.restaurantVoting.model.Vote;
import com.github.lilyarotaru.restaurantVoting.repository.VoteRepository;
import com.github.lilyarotaru.restaurantVoting.to.UserTo;
import com.github.lilyarotaru.restaurantVoting.util.UserUtil;
import com.github.lilyarotaru.restaurantVoting.util.validation.ValidationUtil;
import com.github.lilyarotaru.restaurantVoting.web.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "users")
public class ProfileController extends AbstractUserController {
    static final String REST_URL = "/api/profile";

    @Autowired
    private VoteRepository voteRepository;

    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser.id());
        return authUser.getUser();
    }

    @GetMapping("/vote-today")
    public ResponseEntity<Vote> getTodayVote(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get today's vote");
        return ResponseEntity.of(voteRepository.findByUserId(authUser.id()));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        super.delete(authUser.id());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(allEntries = true)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("register {}", userTo);
        ValidationUtil.checkNew(userTo);
        User created = prepareAndSave(UserUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(allEntries = true)
    public void update(@RequestBody @Valid UserTo userTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} with id={}", userTo, authUser.id());
        ValidationUtil.assureIdConsistent(userTo, authUser.id());
        User user = authUser.getUser();
        prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }
}