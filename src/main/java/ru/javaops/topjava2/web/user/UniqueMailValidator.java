package ru.javaops.topjava2.web.user;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.javaops.topjava2.HasIdAndEmail;
import ru.javaops.topjava2.repository.UserRepository;
import ru.javaops.topjava2.web.GlobalExceptionHandler;
import ru.javaops.topjava2.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;

@Component
@AllArgsConstructor
public class UniqueMailValidator implements org.springframework.validation.Validator {

    private final UserRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        HasIdAndEmail user = ((HasIdAndEmail) target);
        if (StringUtils.hasText(user.getEmail())) {
            repository.getByEmail(user.getEmail().toLowerCase())
                    .ifPresent(dbUser -> {
                        if (request.getMethod().equals("PUT")) {  // UPDATE
                            int dbId = dbUser.id();

                            // it is ok, if update ourself
                            if (user.getId() != null && dbId == user.id()) return;

                            // Workaround for update with user.id=null in request body
                            // ValidationUtil.assureIdConsistent called after this validation
                            String requestURI = request.getRequestURI();
                            if (requestURI.endsWith("/" + dbId) || (dbId == SecurityUtil.authId() && requestURI.contains("/profile"))) return;
                        }
                        errors.rejectValue("email", "", GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL);
                    });
        }
    }
}
