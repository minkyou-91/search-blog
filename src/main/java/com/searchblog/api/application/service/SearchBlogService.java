package com.searchblog.api.application.service;

import com.searchblog.api.application.port.in.dto.SearchBlogComand;
import com.searchblog.api.application.port.in.usecase.SearchBlogUsecase;
import com.searchblog.api.application.port.out.external.ExternalPort;
import com.searchblog.api.application.port.out.persistence.SaveQueryPort;
import com.searchblog.api.domain.SearchBlog;
import com.searchblog.global.dto.BaseResponse;
import com.searchblog.global.enums.ErrorCode;
import com.searchblog.global.exception.CustomException;
import com.searchblog.global.exception.CustomWebClientRequestException;
import com.searchblog.global.exception.CustomWebClientResponseException;
import com.searchblog.global.exception.CustomWebClientTimeoutException;
import com.searchblog.global.factory.ExternalPortFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchBlogService implements SearchBlogUsecase {

    private final ExternalPortFactory externalPortFactory;
    private final SaveQueryPort saveQueryPort;

//    private final KakaoBlogSearchPort kakaoBlogSearchPort;
//    private final NaverBlogSearchPort naverBlogSearchPort;

    @Override
    public BaseResponse searchBlog(SearchBlogComand searchBlogComand) {
        final String query = searchBlogComand.getQuery();
        final String sort = searchBlogComand.getSort();
        final Integer page = searchBlogComand.getPage();
        final Integer size = searchBlogComand.getSize();

        /**
         * PersistencePort saveQuery start
         */
        saveQueryPort.saveQuery(query);

        /**
         * 1. 주입된 ExternalOuputFactory ExternalOutport 우선순위(Priority) 정렬
         * 2. keySet 순서대로 ExternalOutport searchBlog start
         */
        List<Integer> keySet = new ArrayList<>(externalPortFactory.getExternalPortMapper().keySet());
        Collections.sort(keySet);

        SearchBlog searchBlog = null;
        for (Integer key : keySet) {
            if (externalPortFactory.getExternalPortMapper().get(key) instanceof ExternalPort) {
                try {
                    if(key.equals(1)) continue;
                    searchBlog = ((ExternalPort) externalPortFactory.getExternalPortMapper().get(key)).sendBlogSearch(query, sort, page, size);
                    if (!ObjectUtils.isEmpty(searchBlog)) {
                        // 정상 리턴되는 결과가 있다면 break
                        break;
                    }
                } catch (CustomWebClientRequestException | CustomWebClientResponseException |
                         CustomWebClientTimeoutException e) {
                    // 외부 통신 API 문제 발생 시 후처리
                    if (key == externalPortFactory.getExternalPortMapper().size()) { // for문 종료될떄가지 성공하지 못하면 Custom Exception으로 던진다.
                        if (e instanceof WebClientRequestException) {
                            throw new CustomException(ErrorCode.HTTP_STATUS_501);
                        } else if (e instanceof WebClientResponseException) {
                            throw new CustomException(ErrorCode.HTTP_STATUS_503);
                        } else {
                            throw new CustomException(ErrorCode.HTTP_STATUS_504);
                        }
                    }
                } catch (Exception e) {
                    throw new CustomException(ErrorCode.HTTP_STATUS_500, e.getMessage());
                }
            }
        }

        return BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .data(searchBlog).build();
    }
}
