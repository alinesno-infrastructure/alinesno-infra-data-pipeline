package com.alinesno.infra.data.pipeline.core.annotation;

import com.alinesno.infra.data.pipeline.common.type.ProductTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Product {

  ProductTypeEnum value();
}
