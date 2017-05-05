package com.javaeedev.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface Form {

    /**
     * Return as HTML encoding, default is false. Set to true will return html.
     */
    boolean htmlEncoding() default false;

    /**
     * Default value of this property.
     */
    String defaultValue() default "";

    /**
     * If a String field need to be trim(), default is false.
     */
    boolean trim() default false;

}
