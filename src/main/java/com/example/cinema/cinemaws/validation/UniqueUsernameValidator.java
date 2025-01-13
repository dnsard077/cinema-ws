package com.example.cinema.cinemaws.validation;

import com.example.cinema.cinemaws.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageSource messageSource;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.isEmpty()) {
            return true;
        }

        boolean isUnique = userRepository == null || userRepository.findByUsername(username).isEmpty();

        if (!isUnique) {
            context.disableDefaultConstraintViolation();
            String localizedMessage = messageSource.getMessage(
                    context.getDefaultConstraintMessageTemplate().replace("{", "").replace("}", ""),
                    null,
                    LocaleContextHolder.getLocale()
            );
            context.buildConstraintViolationWithTemplate(localizedMessage)
                    .addConstraintViolation();
        }

        return isUnique;
    }
}
