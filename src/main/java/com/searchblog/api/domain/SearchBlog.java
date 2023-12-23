package com.searchblog.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@ApiModel(value = "블로그 검색 도메인", description = "블로그 검색 도메인 & 검색 응답데이터 DTO")
public class SearchBlog {
    @ApiModelProperty(value = "페이지번호", example = "1")
    private Integer page;
    @ApiModelProperty(value = "페이지사이즈", example = "10")
    private Integer size;
    @ApiModelProperty(value = "검색결과 정렬 구분", example = "exact")
    private String sort;
    @ApiModelProperty(value = "검색결과 데이터 개수", example = "100000")
    private Integer totalCount;
    @ApiModelProperty(value = "블로그 게시글 정보들")
    private List<BlogInfo> blogInfos;


    @Getter
    @Builder
    @AllArgsConstructor
    @ApiModel(value = "블로그 게시글 정보")
    public static class BlogInfo {
        @ApiModelProperty(value = "블로그 제목", example = "카카오뱅크")
        private String title;
        @ApiModelProperty(value = "블로그 내용", example = "카카오뱅크에서 근무하고 싶습니다.")
        private String contents;
        @ApiModelProperty(value = "url", example = "https://kakakobank.kakao.com/123")
        private String url;
        @ApiModelProperty(value = "블로그 게시일자", example = "20231223")
        private String postDt;
    }


}
