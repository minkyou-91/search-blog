package com.searchblog.api.application.port.in.usecase;

import com.searchblog.global.dto.BaseResponse;

public interface SearchPopQueryUseCase {
    BaseResponse searchPopQuery(String queryText);
}
