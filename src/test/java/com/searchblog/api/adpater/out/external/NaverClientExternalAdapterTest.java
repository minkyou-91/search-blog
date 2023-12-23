package com.searchblog.api.adpater.out.external;

import com.searchblog.api.application.port.out.dto.NaverClientResponse;
import com.searchblog.api.domain.SearchBlog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("External TEST")
class NaverClientExternalAdapterTest {
    @Autowired
    private NaverClientExternalAdapter naverClientExternalAdapterTest;

    @ParameterizedTest
    @CsvSource({"이효리, exact, 1, 10"})
    @DisplayName("네이버 성공")
    void sendBlogSearchSuccess(String query, String sort, Integer page, Integer size) {
        List<NaverClientResponse.Item> items = new ArrayList<>();

        for(int i=0; i<size; i++) {
            NaverClientResponse.Item item = new NaverClientResponse.Item();
            ReflectionTestUtils.setField(item, "title", "title" + i);
            ReflectionTestUtils.setField(item, "description", "description" + i);
            ReflectionTestUtils.setField(item, "link", "link" + i);
            items.add(item);
        }

        NaverClientResponse naverClientResponse = new NaverClientResponse();
        ReflectionTestUtils.setField(naverClientResponse, "total", size);
        ReflectionTestUtils.setField(naverClientResponse, "items", items);

        //when
        SearchBlog searchBlog = naverClientExternalAdapterTest.sendBlogSearch(query, sort, page, size);

        //then
        assertThat(searchBlog.getBlogInfos().size()).isEqualTo(naverClientResponse.getItems().size());
        assertThat(searchBlog.getSort()).isEqualTo(sort);
        assertThat(searchBlog.getSize()).isEqualTo(size);
        assertThat(searchBlog.getPage()).isEqualTo(page);

    }
}