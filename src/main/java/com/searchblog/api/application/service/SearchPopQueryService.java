package com.searchblog.api.application.service;

import com.searchblog.api.application.port.in.usecase.SearchPopQueryUseCase;
import com.searchblog.api.application.port.out.persistence.SearchPopQueryPort;
import com.searchblog.api.domain.Query;
import com.searchblog.global.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchPopQueryService implements SearchPopQueryUseCase {
    private final SearchPopQueryPort searchPopQueryPort;
    @Override
    public BaseResponse searchPopQuery(String queryText) {
        Query query = searchPopQueryPort.searchPopQuery(queryText);

        return BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .data(query).build();
    }
}
