package com.searchblog.api.application.port.out.external;


public interface NaverBlogSearchPort extends ExternalPort{
    @Override
    default ExternalService getServiceDvdNm(){
        return ExternalService.NAVER;
    };

    @Override
    default Integer getPriority(){
        return 2;
    };
}
