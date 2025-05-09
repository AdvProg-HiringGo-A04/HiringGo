package id.ac.ui.cs.advprog.hiringgo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = AtLeastOneFieldNotBlankValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneFieldNotBlank {

    String message() default "At least one field not blank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

