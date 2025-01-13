package com.example.cinema.cinemaws.validation;

import com.example.cinema.cinemaws.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) {
            return true;
        }

        boolean isUnique = userRepository == null || userRepository.findByEmail(email).isEmpty();

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
