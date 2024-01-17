package com.searchblog.api.application.port.out.persistence;

import com.searchblog.api.domain.Query;

import java.util.List;

/**
 * 인기검색어 조회 포트
 */
public interface SearchPopQueryPort {
    List<Query> searchPopQuery();
}
