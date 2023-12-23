package com.searchblog.api.adpater.in.web;

import com.searchblog.api.application.port.in.dto.SearchBlogComand;
import com.searchblog.api.application.port.in.usecase.SearchBlogUsecase;
import com.searchblog.api.application.port.in.usecase.SearchPopQueryUseCase;
import com.searchblog.global.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchBlogUsecase searchBlogUsecase;
    private final SearchPopQueryUseCase searchPopQueryUseCase;

    @GetMapping("/v1/search/blog")
    public ResponseEntity<BaseResponse> searchBlogRun(SearchBlogComand searchBlogComand){
        return ResponseEntity.ok().body(
                searchBlogUsecase.searchBlog(searchBlogComand)
        );
    }

    @GetMapping("/v1/search/popquery")
    public ResponseEntity<BaseResponse> searchPopQuery(@RequestParam String query){
        return ResponseEntity.ok().body(
            searchPopQueryUseCase.searchPopQuery(query)
        );
    }
}
