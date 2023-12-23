package com.searchblog.api.adpater.in.web;

import com.searchblog.api.application.port.in.dto.SearchBlogComand;
import com.searchblog.api.application.port.in.usecase.SearchBlogUsecase;
import com.searchblog.global.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchBlogUsecase searchBlogUsecase;

    @GetMapping("/v1/search/blog")
    public ResponseEntity<BaseResponse> searchBlogRun(SearchBlogComand searchBlogComand){
        return ResponseEntity.ok().body(
                searchBlogUsecase.searchBlog(searchBlogComand)
        );
    }
}
