package com.searchblog.api.application.port.out.external;


public interface KakaoBlogSearchPort extends ExternalPort {

    @Override
    default ExternalServiceCode getServiceDvdNm() {
        return ExternalServiceCode.KAKAO;
    }

    ;

    @Override
    default Integer getPriority() {
        return 1;
    }

    ;
}
