package com.searchblog.api.application.port.in.dto;

import com.searchblog.global.dto.SelfValidating;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@ApiModel(value = "블로그 검색 Param")
public class SearchBlogComand extends SelfValidating<SearchBlogComand> {
    @ApiParam(name = "query", value = "검색어")
    @NotNull(message = "값이 Null이 될 수 없습니다.")
    private String query;
    @ApiParam(name = "sort", value = "exact(정확도순),latest(최신순)")
    @NotNull(message = "값이 Null이 될 수 없습니다.")
    @Pattern(regexp = "^(exact|latest)$", message = "값은 [exact, latest]만 가능합니다.")
    private String sort;
    @ApiParam(name = "page", value = "페이지번호")
    @NotNull(message = "값이 Null이 될 수 없습니다.")
    @Min(value = 1, message = "값은 1이상이여야 합니다.")
    @Max(value = 50, message = "값은 50이하여야 합니다.")
    private Integer page;
    @ApiParam(name = "size", value = "페이지크기")
    @NotNull(message = "값이 Null이 될 수 없습니다.")
    @Min(value = 1, message = "값은 1이상이여야 합니다.")
    @Max(value = 50, message = "값은 50이하여야 합니다.")
    private Integer size;
}
