package org.jrapidoc.annotation.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotatation can be used on resource method<br/>
 * If more than one return option can be returned use this wrapper for them<br/>
 * <br/>
 * Created by Tomas "sarzwest" Jiricek on 31.3.15.<br/>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Returns {
    Return[] value();
}
