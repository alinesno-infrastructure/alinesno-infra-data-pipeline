package com.alinesno.infra.data.pipeline.api.validate;

import com.alinesno.infra.data.pipeline.api.dto.DatasourceDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConditionalNotBlankValidator implements ConstraintValidator<ConditionalNotBlank, String> {

    private String conditionField;
    private String conditionValue;

    @Override
    public void initialize(ConditionalNotBlank constraintAnnotation) {
        this.conditionField = constraintAnnotation.conditionField();
        this.conditionValue = constraintAnnotation.conditionValue();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 获取当前对象实例
        Object obj = context.getDefaultConstraintMessageTemplate();
        if (obj instanceof DatasourceDto) {
            DatasourceDto dto = (DatasourceDto) obj;

            String readerType = dto.getReaderType() ;

            if(conditionValue.contains(readerType)){
                return true ;
            }
        }

        return false ;
    }
}