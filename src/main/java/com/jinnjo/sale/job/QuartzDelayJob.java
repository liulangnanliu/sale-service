package com.jinnjo.sale.job;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author winner
 * Create_Time 1/14/2019 4:15 PM
 * description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuartzDelayJob {
    String name();
    String group();
    long delayTime() default 0L;
}
