package com.searchblog.api.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Query {
    private String queryText;
    private Long searchCount;
}
