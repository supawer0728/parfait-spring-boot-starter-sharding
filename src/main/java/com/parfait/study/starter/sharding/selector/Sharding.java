package com.parfait.study.starter.sharding.selector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Sharding {
    String spel() default "#this[0]";

    Class<? extends ShardKeyGettable> keyResolverType();
}
