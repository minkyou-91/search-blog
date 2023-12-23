package com.searchblog.api.adpater.in.web;

import com.searchblog.api.application.port.in.dto.SearchBlogComand;
import com.searchblog.api.application.port.in.usecase.SearchBlogUsecase;
import com.searchblog.api.application.port.in.usecase.SearchPopQueryUseCase;
import com.searchblog.api.domain.SearchBlog;
import com.searchblog.global.dto.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"search-apis"})  // search-apis 그룹으로 설정
@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchBlogUsecase searchBlogUsecase;
    private final SearchPopQueryUseCase searchPopQueryUseCase;

    @ApiOperation(value = "블로그 검색", notes = "외부 검색 openAPI 통해 검색 결과 페이징처리")
    @GetMapping("/v1/search/blog")
    public ResponseEntity<BaseResponse<SearchBlog>> searchBlogRun(SearchBlogComand searchBlogComand) {
        return ResponseEntity.ok().body(
                searchBlogUsecase.searchBlog(searchBlogComand)
        );
    }

    @ApiOperation(value = "인기검색어 검색 목록 조회", notes = "사용자들이 많이 검색한 순서대로, 최대 10개의 검색 키워드를 제공")
    @GetMapping("/v1/search/popquery")
    public ResponseEntity<BaseResponse> searchPopQuery() {
        return ResponseEntity.ok().body(
                searchPopQueryUseCase.searchPopQuery()
        );
    }

}
