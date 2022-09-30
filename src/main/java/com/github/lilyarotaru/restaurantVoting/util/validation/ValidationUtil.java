package com.github.lilyarotaru.restaurantVoting.util.validation;

import com.github.lilyarotaru.restaurantVoting.HasId;
import com.github.lilyarotaru.restaurantVoting.error.AppException;
import com.github.lilyarotaru.restaurantVoting.error.IllegalRequestDataException;
import com.github.lilyarotaru.restaurantVoting.web.GlobalExceptionHandler;
import com.github.lilyarotaru.restaurantVoting.web.vote.VoteController;
import lombok.experimental.UtilityClass;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

import java.time.LocalTime;
import java.util.Optional;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

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
        return object.orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Entity with id=" + id + " not found", ErrorAttributeOptions.of(MESSAGE)));
    }

    public static void checkVotingTime(LocalTime votingTime) {
        if (votingTime.isAfter(VoteController.DEADLINE)) {
            throw new AppException(HttpStatus.UNPROCESSABLE_ENTITY, GlobalExceptionHandler.EXCEPTION_CHANGING_VOTE, ErrorAttributeOptions.of(MESSAGE));
        }
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }
}