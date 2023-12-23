package com.searchblog.api.application.port.out.external;

import com.searchblog.api.domain.SearchBlog;

public interface ExternalPort {
    SearchBlog sendBlogSearch(String query, String sort, Integer page, Integer size);

    ExternalServiceCode getServiceDvdNm(); // 서비스구분명(카카오, 네이버)

    Integer getPriority(); // port 우선순위

}
