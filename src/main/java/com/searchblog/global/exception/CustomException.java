package com.searchblog.global.exception;

import com.searchblog.global.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;

    public CustomException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }

    public  CustomException(ErrorCode errorCode, String message){
        this.errorCode = errorCode;
        this.message = message;
    }

}
