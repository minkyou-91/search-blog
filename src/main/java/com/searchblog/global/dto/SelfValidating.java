package com.searchblog.global.dto;

import javax.validation.*;
import java.util.Set;

public abstract class SelfValidating<T> {
    private final Validator validator;

    public SelfValidating() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    /**
     * 인스턴스 속성 모든 Bean 유효성 검사
     */
    public void validateSelf() {
        Set<ConstraintViolation<T>> violations = validator.validate((T) this);
        if (Boolean.FALSE.equals(violations.isEmpty()))
            throw new ConstraintViolationException(violations);
    }
}
