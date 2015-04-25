package org.jrapidoc.annotation.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotatation can be used on resource methods for customizing return option in documentation.<br/>
 * <br/>
 * Created by Tomas "sarzwest" Jiricek on 4.1.15.<br/>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Return {

    /**
     * Http status returned
     * @return
     */
    int http();

    /**
     * Http header names returned
     * @return
     */
    String[] headers() default {};

    /**
     * Http cookie names returned
     * @return
     */
    String[] cookies() default {};

    /**
     * Java type returned
     * @return
     */
    Class<?> type() default Void.class;

    /**
     * Wrapper for {@link org.jrapidoc.annotation.rest.Return#type()}.
     * @return
     */
    Structure structure() default Structure.OBJECT;
    String description() default "";

    enum Structure {
        /**
         * {@link Return#type()} is not wrapped
         */
        OBJECT,
        /**
         * {@link Return#type()} is wrapped into array
         */
        ARRAY,
        /**
         * {@link Return#type()} is wrapped into map as value type (key type is string)
         */
        MAP
    }
}
