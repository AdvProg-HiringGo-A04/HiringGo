package id.ac.ui.cs.advprog.hiringgo.security.annotation;

import id.ac.ui.cs.advprog.hiringgo.enums.Role;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AllowedRoles {
    Role[] value();
}
