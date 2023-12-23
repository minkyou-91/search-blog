package com.searchblog.global.enums;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    HTTP_STATUS_200(HttpStatus.OK, "0000", "정상처리되었습니다."),

    // 400 BAD_REQUEST 잘못된 요청
    HTTP_STATUS_400(HttpStatus.BAD_REQUEST, "2301", "요청 파라미터 값을 다시 확인해 주세요."),
    // 404 NOT_FOUND 잘못된 리소스 접근
    HTTP_STATUS_404(HttpStatus.NOT_FOUND, "2302", "잘못된 리소스 접근 입니다."),
    // 408 REQUEST_TIMEOUT 타임아웃 (인증 토큰 사용 시간 만료)
    HTTP_STATUS_408(HttpStatus.REQUEST_TIMEOUT, "2303", "요청 시간이 초과 되었습니다."),

    // 500 INTERNAL SERVER ERROR
    HTTP_STATUS_500(HttpStatus.INTERNAL_SERVER_ERROR, "9999", "처리 중 오류가 발생 했습니다. 잠시 후 다시 시도 바랍니다."),
    // 501 BAD_GATEWAY (대외계 처리 응답 결과 오류 코드, 서비스에서 외부 API로 유효하지 않은 데이터로 요청한 경우)
    HTTP_STATUS_501(HttpStatus.BAD_GATEWAY, "9001", "대외계 연동 구간 처리 중 오류가 발생 했습니다. 오류가 지속 발생할 경우 관리자에게 문의 부탁 드립니다."),
    // 503 SERVICE UNAVAILABLE (대외계 처리 중 내부 오류 발생.[대외계 서버 작업 또는 프로세스 오류] )
    HTTP_STATUS_503(HttpStatus.SERVICE_UNAVAILABLE, "9002", "대왜계 서버 작업 영향으로 서비스 이용이 원활 하지 않습니다. 오류가 지속 발생할 경우 관리자에게 문의 부탁 드립니다."),
    // 504 GATEWAY TIMEOUT [connection timeout/refused, read timeout, write timeout]
    HTTP_STATUS_504(HttpStatus.GATEWAY_TIMEOUT, "9003", "대외계 통신 연결이 원뢀 하지 않습니다. 잠시 후 다시 시도 바랍니다. 오류가 지속 발생할 경우 관리자에게 문의 부탁 드립니다.");

    private HttpStatus status;
    private String resultCode;
    private String message;

    ErrorCode(HttpStatus status, String resultCode, String message) {
        this.status = status;
        this.resultCode = resultCode;
        this.message = message;
    }

    public static ErrorCode getErrorCode(@NonNull HttpStatus status) {
        for(ErrorCode errorCode : ErrorCode.values()){
            if(errorCode.getStatus().value() == status.value()){
                return errorCode;
            }
        }
        return ErrorCode.HTTP_STATUS_500;
    }
}
