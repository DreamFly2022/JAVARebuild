package com.hef.myreadwritesepabstractrountingv1.conf;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurDataSource {

    String name() default "";

}
