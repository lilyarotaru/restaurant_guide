package com.github.lilyarotaru.restaurantVoting.util.validation;

import com.github.lilyarotaru.restaurantVoting.HasId;
import com.github.lilyarotaru.restaurantVoting.error.IllegalRequestDataException;
import com.github.lilyarotaru.restaurantVoting.error.NotFoundException;
import com.github.lilyarotaru.restaurantVoting.model.Vote;
import com.github.lilyarotaru.restaurantVoting.web.GlobalExceptionHandler;
import com.github.lilyarotaru.restaurantVoting.web.vote.VoteController;
import lombok.experimental.UtilityClass;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;

import java.time.LocalTime;
import java.util.Optional;

import static com.github.lilyarotaru.restaurantVoting.web.GlobalExceptionHandler.NO_VOTE_TODAY;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
    }

    public static <T extends HasId> T checkNotFound(Optional<T> object, int id) {
        return object.orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
    }

    public static Vote checkTodayVote(Optional<Vote> vote) {
        return vote.orElseThrow(() -> new NotFoundException(NO_VOTE_TODAY));
    }

    public static void checkVotingTime(LocalTime votingTime) {
        if (votingTime.isAfter(VoteController.DEADLINE)) {
            throw new IllegalRequestDataException(GlobalExceptionHandler.EXCEPTION_CHANGING_VOTE);
        }
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }
}