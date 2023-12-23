package com.searchblog.api.application.port.in.usecase;

import com.searchblog.api.application.port.in.dto.SearchBlogComand;
import com.searchblog.global.dto.BaseResponse;

public interface SearchBlogUsecase {
    BaseResponse searchBlog(SearchBlogComand searchBlogComand);
}
