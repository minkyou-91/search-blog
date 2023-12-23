package com.searchblog.api.application.port.out.external;


public interface NaverBlogSearchPort extends ExternalPort {
    @Override
    default ExternalServiceCode getServiceDvdNm() {
        return ExternalServiceCode.NAVER;
    }

    ;

    @Override
    default Integer getPriority() {
        return 2;
    }

    ;
}
