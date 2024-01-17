package com.searchblog.api.adpater.out.external;

import com.searchblog.api.application.port.out.dto.KakaoClientResponse;
import com.searchblog.api.domain.SearchBlog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@DisplayName("Exteranl TEST")
class KakaoClientExternalAdapterTest {
    @Autowired
    private KakaoClientExternalAdapter kakaoClientExternalAdapterTest;

    @ParameterizedTest
    @CsvSource({"이효리, exact, 1, 10"})
    @DisplayName("카카오 성공")
    void sendBlogSearchSuccess(String query, String sort, Integer page, Integer size) {

        // given
        List<KakaoClientResponse.Document> documents = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            KakaoClientResponse.Document document = new KakaoClientResponse.Document();
            ReflectionTestUtils.setField(document, "title", "title" + i);
            ReflectionTestUtils.setField(document, "contents", "contents" + i);
            ReflectionTestUtils.setField(document, "url", "url" + i);
            documents.add(document);
        }

        KakaoClientResponse.Meta meta = new KakaoClientResponse.Meta();
        ReflectionTestUtils.setField(meta, "totalCount", size);

        KakaoClientResponse kakaoClientResponse = new KakaoClientResponse();
        ReflectionTestUtils.setField(kakaoClientResponse, "documents", documents);
        ReflectionTestUtils.setField(kakaoClientResponse, "meta", meta);

        //when
        SearchBlog searchBlog = kakaoClientExternalAdapterTest.sendBlogSearch(query, sort, page, size);

        //then
        assertThat(searchBlog.getBlogInfos().size()).isEqualTo(kakaoClientResponse.getDocuments().size());
        assertThat(searchBlog.getSort()).isEqualTo(sort);
        assertThat(searchBlog.getSize()).isEqualTo(size);
        assertThat(searchBlog.getPage()).isEqualTo(page);
    }
}