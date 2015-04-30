package org.jrapidoc.annotation.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotatation can be used for indication whether parameter is required or optional<br/>
 * <br/>
 * Created by Tomas "sarzwest" Jiricek on 16.4.15.<br/>
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsRequired {
    /**
     * true if required<br/>
     * false if optional<br/>
     * @return
     */
    boolean value() default true;
}
