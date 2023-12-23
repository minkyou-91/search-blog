package com.searchblog.api.application.port.out.external;

import com.searchblog.api.domain.SearchBlog;

public interface ExternalPort {
    SearchBlog sendBlogSearch(String query, String sort, Integer page, Integer size);
    ExternalService getServiceDvdNm();

    Integer getPriority();

}
