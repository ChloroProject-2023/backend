package com.usth.edu.vn.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.FIELD
})
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "Password does not meet the requirement!";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};
}
