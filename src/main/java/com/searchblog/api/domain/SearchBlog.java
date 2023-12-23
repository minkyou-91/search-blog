package com.searchblog.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchBlog {
    private Integer page;
    private Integer size;
    private String sort;
    private Integer totalCount;
    private List<BlogInfo> blogInfos;


    @Getter
    @Builder
    @AllArgsConstructor
    public static class BlogInfo {
        private String title;
        private String contents;
        private String url;
        private String postDt;
    }




}
