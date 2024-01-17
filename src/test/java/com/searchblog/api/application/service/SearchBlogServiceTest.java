package com.searchblog.api.application.service;

import com.searchblog.api.adpater.out.external.KakaoWebclient;
import com.searchblog.api.application.port.in.dto.SearchBlogComand;
import com.searchblog.api.application.port.out.dto.KakaoClientResponse;
import com.searchblog.api.application.port.out.external.ExternalPort;
import com.searchblog.api.application.port.out.external.KakaoBlogSearchPort;
import com.searchblog.api.application.port.out.external.NaverBlogSearchPort;
import com.searchblog.api.application.port.out.persistence.SaveQueryPort;
import com.searchblog.api.domain.SearchBlog;
import com.searchblog.global.dto.BaseResponse;
import com.searchblog.global.factory.ExternalPortFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 유스케이스 테스트
 *
 * << 외부 API 통신 중 오류 발생 하였을때 >>
 * 우선순위에 따라 카카오 outport -> naver outport로 수행하는 테스트는 production 소스에서 에러 발생시켜 전환여부 확인하였습니다.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UseCase Test")
public class SearchBlogServiceTest {

    @InjectMocks
    SearchBlogService searchBlogService;

    @Mock
    KakaoBlogSearchPort kakaoBlogSearchPort;

    @Mock
    NaverBlogSearchPort naverBlogSearchPort;

    @Mock
    ExternalPortFactory externalPortFactory;

    @Mock
    SaveQueryPort saveQueryPort;

    List<ExternalPort> externalPorts;

    @BeforeEach
    void externalPortSetup(){
        externalPorts = new ArrayList<>();
        externalPorts.add(kakaoBlogSearchPort);
        externalPorts.add(naverBlogSearchPort);

//        this.externalPortFactory.getExternalPortMapper().put(1, this.kakaoBlogSearchPort);
//        this.externalPortFactory.getExternalPortMapper().put(2, this.naverBlogSearchPort);
    }

    @ParameterizedTest
    @DisplayName("[블로그 검색 - 성공]")
    @CsvSource({"이효리, exact, 1, 10"})
    void searchBlogRunSuccess(String query, String sort, Integer page, Integer size) {
        SearchBlogComand searchBlogComand = new SearchBlogComand();
        ReflectionTestUtils.setField(searchBlogComand, "query", query);
        ReflectionTestUtils.setField(searchBlogComand, "sort", sort);
        ReflectionTestUtils.setField(searchBlogComand, "page", page);
        ReflectionTestUtils.setField(searchBlogComand, "size", size);

        List<SearchBlog.BlogInfo> blogInfos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            blogInfos.add(SearchBlog.BlogInfo.builder()
                    .title("testtitle" + i)
                    .url("testurl" + i)
                    .contents("testContent" + i)
                    .postDt("20231223").build());
        }

        SearchBlog searchBlog = SearchBlog.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .blogInfos(blogInfos).build();

        externalPortFactory = new ExternalPortFactory(externalPorts);


        for(Integer key : externalPortFactory.getExternalPortMapper().keySet()){
            Mockito.lenient().when(((ExternalPort) externalPortFactory.getExternalPortMapper().get(key)).sendBlogSearch(query, sort, page, size))
                    .thenReturn(searchBlog);

            //given
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .data(searchBlog)
                    .build();

            //when
            BaseResponse response = searchBlogService.searchBlog(searchBlogComand);

            //then
            assertThat(response.getStatus()).isEqualTo(baseResponse.getStatus());
            assertThat(response.getResultCode()).isEqualTo("0000");
            assertThat(response.getMessage()).isEqualTo(baseResponse.getMessage());
        }
    }
}
