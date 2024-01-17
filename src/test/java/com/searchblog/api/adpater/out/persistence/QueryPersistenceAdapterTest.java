package com.searchblog.api.adpater.out.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({QueryPersistenceAdapter.class, QueryMapper.class})
@DisplayName("Persistence TEST")
class QueryPersistenceAdapterTest {

    @Autowired
    private QueryPersistenceAdapter queryPersistenceAdapterTest;
    @Autowired
    private QueryMapper queryMapper;
    @Autowired
    private QueryRepository queryRepository;

    @Test
    @DisplayName("query 초기 save 성공")
    void insertQuerySuccess(){
        String queryText = "이효리";
        queryPersistenceAdapterTest.saveQuery(queryText);

        assertThat(queryRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("query searchCount update 성공")
    void updateCountSuccess(){
        String queryText = "이효리";
        queryPersistenceAdapterTest.saveQuery(queryText);

        queryPersistenceAdapterTest.saveQuery(queryText);

        assertThat(queryRepository.count()).isEqualTo(1);

        assertThat(queryRepository.findByQueryText(queryText).get().getSearchCount()).isEqualTo(2);

    }

}