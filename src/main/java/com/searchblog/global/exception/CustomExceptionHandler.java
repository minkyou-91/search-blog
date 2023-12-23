package com.searchblog.global.exception;

import com.searchblog.global.dto.ErrorResponse;
import com.searchblog.global.enums.ErrorCode;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;

@Log4j2
@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * 커스텀 예외 처리 핸들러
     * @param ex
     * @return
     */
    @ExceptionHandler
    protected ResponseEntity<?> handleCustomException(CustomException ex){
        this.logWriteExceptionStackTrace(ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(ex.getErrorCode().getStatus())
                .message(Strings.isNotBlank(ex.getMessage()) ? ex.getMessage() : ex.getErrorCode().getMessage())
                .error(ex.getErrorCode().getStatus().series().name().toLowerCase())
                .build();
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    /**
     * 필수 파라미터 검증 오류 핸들러
     * @param ex
     * @return
     */
    @ExceptionHandler({ ConstraintViolationException.class })
    protected ResponseEntity<?> handleServerException(ConstraintViolationException ex) {
        this.logWriteExceptionStackTrace(ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(ErrorCode.HTTP_STATUS_400.getStatus())
                .message(Strings.isNotBlank(ex.getMessage()) ? ex.getMessage() : ErrorCode.HTTP_STATUS_400.getMessage())
                .error(ErrorCode.HTTP_STATUS_400.getStatus().series().name().toLowerCase())
                .build();
        return ResponseEntity.status(ErrorCode.HTTP_STATUS_400.getStatus()).body(errorResponse);
    }

    /**
     * 핸들링 되지 못한 예외 일괄 응답 처리
     * @param ex
     * @return
     */
    @ExceptionHandler({ Exception.class })
    protected ResponseEntity<?> handleServerException(Exception ex) {
        this.logWriteExceptionStackTrace(ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(ErrorCode.HTTP_STATUS_500.getStatus())
                .message(ErrorCode.HTTP_STATUS_500.getMessage())
                .error(ErrorCode.HTTP_STATUS_500.getStatus().series().name().toLowerCase())
                .build();
        return ResponseEntity.status(ErrorCode.HTTP_STATUS_500.getStatus()).body(errorResponse);
    }

    private void logWriteExceptionStackTrace(Throwable e){
        if(e instanceof CustomException) {
            CustomException ex = (CustomException) e;
            log.error("CustomException: status:{} - {}", ex.getErrorCode(), ex.getMessage());
        } else {
            log.error("{} - {}", e.getClass().getSimpleName(), e.getMessage());
        }
        final StringBuilder sb = new StringBuilder("Print Stack Trace: \n");
        Arrays.stream(e.getStackTrace()).iterator().forEachRemaining(
                se -> sb.append("    ")
                        .append(se.getClassName()).append("(")
                        .append(se.getMethodName()).append(":")
                        .append(se.getLineNumber()).append(")").append("\n"));
        log.error(sb.toString());
    }
}
