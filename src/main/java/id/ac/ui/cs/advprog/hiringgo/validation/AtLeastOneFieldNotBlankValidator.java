package id.ac.ui.cs.advprog.hiringgo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class AtLeastOneFieldNotBlankValidator implements ConstraintValidator<AtLeastOneFieldNotBlank, Object> {

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return false;
        }

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                if (value != null) {
                    return true;
                }
            } catch (IllegalAccessException ignored) {
            }
        }

        return false;
    }
}
