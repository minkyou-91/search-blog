package com.searchblog.api.application.port.out.dto;

import com.searchblog.api.domain.SearchBlog;

public interface ClientResponseMapper {
    SearchBlog toDomain(ClientResponse clientResponse, String sort, Integer page, Integer size);
}
