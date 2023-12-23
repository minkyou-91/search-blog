package com.searchblog.global.dto;

import com.searchblog.global.enums.ErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@ApiModel(value = "에러응답결과", description = "공통 에러 응답 DTO")
public class ErrorResponse {
    @ApiModelProperty(value = "HTTP status code", example = "200")
    Integer status;
    @ApiModelProperty(value = "error", example = "CLIENT_ERROR")
    String error;
    @ApiModelProperty(value = "응답결과코드", example = "9999")
    String resultCode;
    @ApiModelProperty(value = "error message", example = "값이 NULL이 될 수 없습니다.")
    String message;

    @Builder
    public ErrorResponse(HttpStatus httpStatus, String error, String resultCode, String message) {
        this.status = httpStatus.value();
        this.error = error;
        this.resultCode = this.resultCodeSet(httpStatus);
        this.message = message;
    }

    private String resultCodeSet(HttpStatus httpStatus) {
        for (ErrorCode o : ErrorCode.values()) {
            if (o.getStatus().equals(httpStatus)) {
                return o.getResultCode();
            }
        }
        return "정의되지 않은 에러코드입니다.";
    }

}
