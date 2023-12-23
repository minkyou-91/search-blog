package com.searchblog.api.application.port.in.dto;

import com.searchblog.global.dto.SelfValidating;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SearchPopQueryCommand extends SelfValidating<SearchPopQueryCommand> {
    private String query;
}
