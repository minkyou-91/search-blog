package com.searchblog.global.dto;

import com.searchblog.global.enums.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class BaseResponse<T> {

    private final Integer status;
    private final String message;
    private final String resultCode;
    private final T data;

    @Builder
    public BaseResponse(@NonNull HttpStatus httpStatus, T data) {
        this.status = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
        this.resultCode = this.resultCodeSet(httpStatus);
        this.data = data;
    }

    private String resultCodeSet(HttpStatus httpStatus) {
        for(ErrorCode o : ErrorCode.values()){
            if(o.getStatus().equals(httpStatus)){
                return o.getResultCode();
            }
        }
        return "정의되지 않은 에러코드입니다.";
    }

}
