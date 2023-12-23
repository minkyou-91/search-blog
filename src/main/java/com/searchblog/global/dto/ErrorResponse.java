package com.searchblog.global.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class ErrorResponse {
    Integer status;
    String error;
    String message;

    @Builder
    public ErrorResponse(HttpStatus httpStatus, String error, String message) {
        this.status = httpStatus.value();
        this.error = error;
        this.message = message;
    }

}
