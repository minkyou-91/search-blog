package com.searchblog.api.application.port.out.persistence;

/**
 * 블로그 검색시 검색어 저장 포트
 */
public interface SaveQueryPort {
    void saveQuery(String query);
}
