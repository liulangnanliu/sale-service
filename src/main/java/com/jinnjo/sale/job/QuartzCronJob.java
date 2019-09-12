package com.jinnjo.sale.job;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author winner
 * Create_Time 10/19/2018 2:03 PM
 * description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuartzCronJob {
    String name();
    String group();
    String defaultCronExpression();
}
