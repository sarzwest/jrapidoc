package org.jrapidoc.annotation.soap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotatation can be used on web service method<br/>
 * It is used for customizing Java return type<br/>
 * <br/>
 * Created by Tomas "sarzwest" Jiricek on 11.4.15.<br/>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Return {
    /**
     * http status of returned message
     *
     * @return
     */
    int http() default 200;

    /**
     * Description of Java return type
     * @return
     */
    String description() default "";
}
