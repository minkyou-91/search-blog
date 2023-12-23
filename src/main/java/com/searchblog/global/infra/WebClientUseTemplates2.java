package com.searchblog.global.infra;

import com.searchblog.global.exception.CustomWebClientRequestException;
import com.searchblog.global.exception.CustomWebClientResponseException;
import com.searchblog.global.exception.CustomWebClientTimeoutException;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import lombok.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Consumer;

public class WebClientUseTemplates2 {
    public Object httpGetSendRetrieve(@NonNull WebClient webClient, @NonNull Data data) {
        try {
            final WebClient.RequestHeadersSpec<?> requestHeadersSpec =
                    webClient.method(HttpMethod.GET)
                            .uri(data.getConnUrl())
                            .headers(data.getHeaders());
            return this.httpResponse(requestHeadersSpec, data);

        } catch (WebClientRequestException ex) {
            if ((ex.getCause() instanceof ConnectTimeoutException)
                    || (ex.getCause() instanceof ReadTimeoutException)
                    || (ex.getCause() instanceof WriteTimeoutException)) {
                throw new CustomWebClientTimeoutException(data.getConnUrl(), ex.getCause());
            }
            throw ex;
        }
    }

    public Object httpResponse(@NonNull WebClient.RequestHeadersSpec<?> requestHeadersSpec, Data data) {
        return requestHeadersSpec.retrieve()
                /* HTTP Status 4XX Client Error */
                .onStatus(HttpStatus::is4xxClientError, response ->
                        Mono.error(new CustomWebClientRequestException(response, data.getMethod(), data.getConnUrl(), data.getResponseSpecBodyClass())))
                /* HTTP Status 5XX Server Error */
                .onStatus(HttpStatus::is5xxServerError, response ->
                        Mono.error(new CustomWebClientResponseException(response, data.getMethod(), data.getConnUrl(), data.getResponseSpecBodyClass())))
                /* Success */
                .bodyToMono(data.getResponseSpecBodyClass()) // 응답 받을 Class 설정.
                .block();
    }

    @Getter
    @Builder
    @Value
    @ToString
    public static class Data {
        HttpMethod method;
        URI connUrl;
        Consumer<HttpHeaders> headers;
        Class<?> responseSpecBodyClass;
    }
}
