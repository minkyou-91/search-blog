package com.searchblog.global.exception;

import com.searchblog.global.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;

@Setter
@Getter
public class CustomWebClientTimeoutException extends RuntimeException {
    private final ErrorCode errorCode;
    private final URI uri;
    private final Throwable ex;
    public CustomWebClientTimeoutException(URI uri, Throwable ex) {
        this.errorCode = ErrorCode.HTTP_STATUS_504;
        this.uri=uri;
        this.ex=ex;
    }
}
