package com.searchblog.api.application.port.out.external;


public interface KakaoBlogSearchPort extends ExternalPort {

    @Override
    default ExternalService getServiceDvdNm(){
        return ExternalService.KAKAO;
    };

    @Override
    default Integer getPriority(){
        return 1;
    };
}
