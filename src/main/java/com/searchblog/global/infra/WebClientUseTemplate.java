package com.searchblog.global.infra;

import com.searchblog.global.exception.CustomWebClientRequestException;
import com.searchblog.global.exception.CustomWebClientResponseException;
import com.searchblog.global.exception.CustomWebClientTimeoutException;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.URLEncoder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class WebClientUseTemplate {

    public <T> T httpGetSendRetrieve(@NonNull WebClient webClient, @NonNull URI connUrl, @NonNull Class<T> responseBodySpec) {
        try {
            String sendUrl = URLDecoder.decode(String.valueOf(connUrl), StandardCharsets.UTF_8);

            return webClient.get().uri(sendUrl)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        Mono.error(new CustomWebClientRequestException(response, HttpMethod.GET, connUrl, responseBodySpec)))
                .onStatus(HttpStatus::is5xxServerError, response ->
                        Mono.error(new CustomWebClientResponseException(response, HttpMethod.GET, connUrl, responseBodySpec)))
                .bodyToMono(responseBodySpec)
                .block();
        } catch (WebClientRequestException ex) {
            if ((ex.getCause() instanceof ConnectTimeoutException)
                    || (ex.getCause() instanceof ReadTimeoutException)
                    || (ex.getCause() instanceof WriteTimeoutException)) {
                throw new CustomWebClientTimeoutException(null, ex.getCause());
            }
            throw ex;
        }
    }
}
