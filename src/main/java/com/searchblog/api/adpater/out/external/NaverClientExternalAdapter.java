package com.searchblog.api.adpater.out.external;

import com.searchblog.api.application.port.out.dto.ClientResponse;
import com.searchblog.api.application.port.out.dto.ClientResponseMapper;
import com.searchblog.api.application.port.out.dto.NaverClientResponse;
import com.searchblog.api.application.port.out.external.NaverBlogSearchPort;
import com.searchblog.api.domain.SearchBlog;
import com.searchblog.global.enums.ParamCommand;
import com.searchblog.global.infra.WebClientUseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NaverClientExternalAdapter implements NaverBlogSearchPort, ClientResponseMapper {

    @Qualifier(value = "naverClient")
    private final WebClient naverClient;
    private final WebClientUseTemplate webClientUseTemplate;


    @Override
    public SearchBlog sendBlogSearch(String query, String sort, Integer page, Integer size) {
        URI connUrl = UriComponentsBuilder.fromUriString("/v1/search/blog.json")
                .queryParam("query", query)
                .queryParam("display", size)
                .queryParam("start", page)
                .queryParam("sort", ParamCommand.getParamCommand(sort).getNaverSortVal())
                .build().toUri();

        NaverClientResponse response = webClientUseTemplate.httpGetSendRetrieve(naverClient, connUrl, NaverClientResponse.class);

//        NaverClientResponse response = naverClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/v1/search/blog.json")
//                        .queryParam("query", query)
//                        .queryParam("display", size)
//                        .queryParam("start", page)
//                        .queryParam("sort", ParamCommand.getParamCommand(sort).getNaverSortVal())
//                        .build())
//                .retrieve()
//                .bodyToMono(NaverClientResponse.class)
//                .block();

        return this.toDomain(response, sort, page, size);
    }

    @Override
    public SearchBlog toDomain(ClientResponse clientResponse, String sort, Integer page, Integer size) {
        return SearchBlog.builder()
                .sort(sort)
                .page(page)
                .size(size)
                .totalCount(((NaverClientResponse) clientResponse).getTotal())
                .blogInfos(((NaverClientResponse) clientResponse).getItems().stream()
                        .map(item -> SearchBlog.BlogInfo.builder()
                                .title(item.getTitle())
                                .url(item.getLink())
                                .contents(item.getDescription())
                                .postDt(item.getPostdate()).build()).collect(Collectors.toList())).build();
    }
}
