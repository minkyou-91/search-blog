package com.searchblog.api.application.port.out.persistence;

import com.searchblog.api.domain.Query;

public interface SearchPopQueryPort {
    Query searchPopQuery(String queryText);
}
