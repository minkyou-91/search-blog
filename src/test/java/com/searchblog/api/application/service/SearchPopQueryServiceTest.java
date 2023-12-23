package com.searchblog.api.application.service;

import com.searchblog.api.adpater.out.persistence.QueryEntity;
import com.searchblog.api.adpater.out.persistence.QueryRepository;
import com.searchblog.api.application.port.out.persistence.SearchPopQueryPort;
import com.searchblog.global.dto.BaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("UseCase Test")
class SearchPopQueryServiceTest {

    @InjectMocks
    SearchPopQueryService searchPopQueryService;

    @Mock
    QueryRepository queryRepository;

    @Mock
    SearchPopQueryPort searchPopQueryPort;

        @Test
        @DisplayName("인기 검색어 조회 - 성공")
        void searchPopQuerySuccess() {

            // given
            List<QueryEntity> searchQueries = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                searchQueries.add(QueryEntity.builder().queryText("테스트"+i).build());
            }

            // when
            Mockito.lenient().when(queryRepository.findTop10ByOrderBySearchCountDesc()).thenReturn(searchQueries);

            BaseResponse baseResponse = searchPopQueryService.searchPopQuery();

            assertThat(baseResponse.getStatus()).isEqualTo(200);
            assertThat(baseResponse.getResultCode()).isEqualTo("0000");
            assertThat(baseResponse.getMessage()).isEqualTo("OK");
        }



}