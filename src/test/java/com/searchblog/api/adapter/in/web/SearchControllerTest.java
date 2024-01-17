package com.searchblog.api.adapter.in.web;

import com.searchblog.api.adpater.out.persistence.QueryEntity;
import com.searchblog.global.enums.ErrorCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("통합테스트 - 웹 어댑터")
class SearchControllerTest {

    private static final String LIST_POPULAR_QUERIS_URL = "/v1/search/popquery";
    private static final String BLOG_SEARCH_URL = "/v1/search/blog";
    private static final String[] MOCK_QUERIES = new String[] {"intellij", "스프링부트", "sqld", "postgresql", "mysql", "카카오", "naver", "테스트코드", "레디스", "kafka", "rabbitmq", "spring boot", "gradle 그레이들", "시스템 설계", "db 인덱스"};
    private static final Long[] MOCK_SEARCH_COUNTS = new Long[] {482402858L, 274960282L, 94284283L, 58692383L, 52352341L, 7293482L, 4451345L, 64313L, 54334L, 9353L, 7532L, 3341L, 245L, 42L, 8L};

    @Autowired(required = false)
    private MockMvc mockMvc;

    @Autowired
    private EntityManagerFactory emf;

    @BeforeAll
    void init() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        for (int i = 0; i < MOCK_QUERIES.length; i++) {
            QueryEntity searchQuery = QueryEntity.builder().queryText(MOCK_QUERIES[i]).build();
            ReflectionTestUtils.setField(searchQuery, "searchCount", MOCK_SEARCH_COUNTS[i]);
            em.persist(searchQuery);
        }
        tx.commit();
    }

    @Nested
    @DisplayName("블로그 검색")
    class Search {
        @ParameterizedTest
        @DisplayName("검색 API 컨트롤러 - 성공")
        @CsvSource(value = {"이효리, exact, 1, 10", "이효리, exact, 2, 5", "이효리, exact, 3, 50"}, nullValues = "null")
        void searchBlogSuccess(String query, String sort, Integer page, Integer size) throws Exception {
            MultiValueMap<String, String> parmas = createParmas(query, sort, page, size);
            performGet(BLOG_SEARCH_URL, parmas)
                    .andExpect(jsonPath("$.status", is(200)))
                    .andExpect(jsonPath("$.resultCode", is("0000")))
                    .andExpect(jsonPath("$.message", is("OK")))
                    .andExpect(jsonPath("$.data.page", is(page)))
                    .andExpect(jsonPath("$.data.size", is(size)))
                    .andExpect(jsonPath("$.data.totalCount", greaterThan(0)))
                    .andExpect(jsonPath("$.data.blogInfos", hasSize(size)))
            ;
        }

        @ParameterizedTest
        @DisplayName("파라미터 검증 - 실패")
        @CsvSource(value = {"이효리, test, 1, 10", "이효리, exact, 0, 10", "이효리, exact, 51, 10", "이효리, accuracy, 1, 0", "이효리, accuracy, 1, 51"})
        void testFailConstraintViolation(String query, String sort, Integer page, Integer size) throws Exception {
            MultiValueMap<String, String> parmas = createParmas(query, sort, page, size);
            performGet(BLOG_SEARCH_URL, parmas)
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.resultCode", is(ErrorCode.HTTP_STATUS_400.getResultCode())))
            ;
        }

    }


    @Nested
    @DisplayName("인기 검색어 조회")
    class PopularQueries {

        @DisplayName("블로그 검색에 따른 검색횟수 증가")
        @ParameterizedTest
        @CsvSource({"0", "1", "2", "3"})
        void queryUpdate(Integer count) throws Exception {

            MultiValueMap<String, String> params = createParmas(MOCK_QUERIES[count], "accuracy", 1, 10);
            for (Integer i = 0; i < count; i++) {
                performGet(BLOG_SEARCH_URL, params);
            }

            performGet(LIST_POPULAR_QUERIS_URL)
                    .andExpect(jsonPath("$.status", is(200)))
                    .andExpect(jsonPath("$.resultCode", is("0000")))
                    .andExpect(jsonPath("$.message", is("OK")))
                    .andExpect(jsonPath("$.data", hasSize(10)))
                    .andExpect(jsonPath(String.format("$.data[%s].queryText", count), is(MOCK_QUERIES[count])))
                    .andExpect(jsonPath(String.format("$.data[%s].searchCount", count), is(MOCK_SEARCH_COUNTS[count].intValue())))
            ;
        }
    }

    private MultiValueMap<String, String> createParmas(String query, String sort, Integer page, Integer size) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (query != null) {
            params.set("query", query);
        }
        if (sort != null) {
            params.set("sort", sort);
        }
        if (page != null) {
            params.set("page", page.toString());
        }
        if (size != null) {
            params.set("size", size.toString());
        }

        return params;
    }

    private ResultActions performGet(String url) {
        return performGet(url, new LinkedMultiValueMap<>());
    }

    private ResultActions performGet(String url, MultiValueMap<String, String> params) {
        try {
            return mockMvc.perform(
                    get(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", new StringBuffer().append("KakaoAK").append(" ").append("38ed4f80289fa9d5997843af07e536d0").toString())
                            .params(params)
            ).andDo(print());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
