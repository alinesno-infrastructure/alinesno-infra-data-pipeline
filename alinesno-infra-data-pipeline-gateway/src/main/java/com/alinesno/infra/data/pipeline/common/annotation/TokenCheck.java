package com.alinesno.infra.data.pipeline.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenCheck {

  boolean needCheck() default true;
}
