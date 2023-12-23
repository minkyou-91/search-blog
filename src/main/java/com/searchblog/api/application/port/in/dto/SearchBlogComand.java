package com.searchblog.api.application.port.in.dto;

import com.searchblog.global.dto.SelfValidating;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class SearchBlogComand extends SelfValidating<SearchBlogComand> {
    @NotBlank(message = "값이 Null이 될 수 없습니다.")
    private String query;
    @NotBlank(message = "값이 Null이 될 수 없습니다.")
    private String sort;
    @Min(value = 1, message = "값은 1이상이여야 합니다.")
    @Max(value = 50, message = "값은 50이하여야 합니다.")
    private Integer page;
    @Min(value = 1, message = "값은 1이상이여야 합니다.")
    @Max(value = 50, message = "값은 50이하여야 합니다.")
    private Integer size;
}
