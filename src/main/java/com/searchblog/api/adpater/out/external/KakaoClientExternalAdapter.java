package com.searchblog.api.adpater.out.external;

import com.searchblog.api.application.port.out.dto.ClientResponse;
import com.searchblog.api.application.port.out.dto.ClientResponseMapper;
import com.searchblog.api.application.port.out.dto.KakaoClientResponse;
import com.searchblog.api.application.port.out.external.KakaoBlogSearchPort;
import com.searchblog.api.domain.SearchBlog;
import com.searchblog.global.enums.ParamCommand;
import com.searchblog.global.exception.CustomWebClientRequestException;
import com.searchblog.global.exception.CustomWebClientResponseException;
import com.searchblog.global.infra.WebClientUseTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Log4j2
@Component
@RequiredArgsConstructor
public class KakaoClientExternalAdapter implements KakaoBlogSearchPort, ClientResponseMapper {
    @Qualifier(value = "kakaoClient")
    private final WebClient kakaoClient;
    private final WebClientUseTemplate webClientUseTemplate;

    @Override
    public SearchBlog sendBlogSearch(String query, String sort, Integer page, Integer size) {
        URI connUrl = UriComponentsBuilder.fromUriString("/v2/search/log")
                .queryParam("query", query)
                .queryParam("sort", ParamCommand.getParamCommand(sort).getKakaoSortVal())
                .queryParam("page", page)
                .queryParam("size", size)
                .build().toUri();

        KakaoClientResponse response = webClientUseTemplate.httpGetSendRetrieve(kakaoClient, connUrl, KakaoClientResponse.class);

        return this.toDomain(response, sort, page, size);
    }

    @Override
    public SearchBlog toDomain(ClientResponse clientResponse, String sort, Integer page, Integer size) {
        return SearchBlog.builder()
                .sort(sort)
                .page(page)
                .size(size)
                .totalCount(((KakaoClientResponse) clientResponse).getMeta().getTotalCount())
                .blogInfos(((KakaoClientResponse) clientResponse).getDocuments().stream()
                        .map(document -> SearchBlog.BlogInfo.builder()
                                .title(document.getTitle())
                                .url(document.getUrl())
                                .contents(document.getContents())
                                .postDt(document.getDatetime().format(DateTimeFormatter.ofPattern("yyyyMMdd"))).build()).collect(Collectors.toList())).build();
    }
}
