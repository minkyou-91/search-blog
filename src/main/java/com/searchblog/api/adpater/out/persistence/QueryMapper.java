package com.searchblog.api.adpater.out.persistence;

import com.searchblog.api.domain.Query;
import org.springframework.stereotype.Component;

@Component
public class QueryMapper {

    public Query toDomain(QueryEntity queryEntity) {
        return Query.builder()
                .queryText(queryEntity.getQueryText())
                .searchCount(queryEntity.getSearchCount())
                .build();
    }

    public QueryEntity toEntity(Query query) {
        return QueryEntity.builder()
                .queryText(query.getQueryText())
                .searchCount(query.getSearchCount())
                .build();
    }


}
