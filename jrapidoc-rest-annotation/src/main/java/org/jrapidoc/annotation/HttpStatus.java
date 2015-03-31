package org.jrapidoc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by papa on 4.1.15.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpStatus {
    int http();
    Header[] headers() default {};
    Cookie[] cookies() default {};
    ReturnTypes[] types();
}
