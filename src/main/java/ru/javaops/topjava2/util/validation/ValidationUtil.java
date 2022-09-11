package ru.javaops.topjava2.util.validation;

import lombok.experimental.UtilityClass;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import ru.javaops.topjava2.HasId;
import ru.javaops.topjava2.error.AppException;
import ru.javaops.topjava2.error.IllegalRequestDataException;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.web.GlobalExceptionHandler;

import java.time.LocalTime;
import java.util.function.Supplier;

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

    public static Supplier<AppException> notFoundWithId(int id) {
        return () -> new AppException(HttpStatus.NOT_FOUND, "Entity with id=" + id + " not found", ErrorAttributeOptions.of(MESSAGE));
    }

    public static void checkVotingTime(LocalTime votingTime) {
        if (votingTime.isAfter(Vote.DEADLINE)) {
            throw new DataIntegrityViolationException(GlobalExceptionHandler.EXCEPTION_CHANGING_VOTE);
        }
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }
}