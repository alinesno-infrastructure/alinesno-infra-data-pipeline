package com.alinesno.infra.data.pipeline.api.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConditionalNotBlankValidator.class)
public @interface ConditionalNotBlank {

    String message() default "此字段在指定条件下不能为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String conditionField();

    String conditionValue();
}