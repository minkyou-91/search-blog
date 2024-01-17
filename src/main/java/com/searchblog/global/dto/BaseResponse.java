package com.searchblog.global.dto;

import com.searchblog.global.enums.ErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@ApiModel(value = "응답결과", description = "공통 응답 DTO")
public class BaseResponse<T> {

    @ApiModelProperty(value = "HTTP status code", example = "200")
    private final Integer status;
    @ApiModelProperty(value = "응답 메시지 - HTTP STATUS MESSAGE", example = "OK")
    private final String message;
    @ApiModelProperty(value = "응답결과코드", example = "0000")
    private final String resultCode;
    @ApiModelProperty(value = "응답데이터")
    private final T data;

    @Builder
    public BaseResponse(@NonNull HttpStatus httpStatus, T data) {
        this.status = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
        this.resultCode = this.resultCodeSet(httpStatus);
        this.data = data;
    }

    public String resultCodeSet(HttpStatus httpStatus) {
        for(ErrorCode o : ErrorCode.values()){
            if(o.getStatus().equals(httpStatus)){
                return o.getResultCode();
            }
        }
        return "정의되지 않은 에러코드입니다.";
    }

}
