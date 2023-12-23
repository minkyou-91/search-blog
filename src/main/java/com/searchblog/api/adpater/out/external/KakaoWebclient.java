package com.searchblog.api.adpater.out.external;

import com.searchblog.global.infra.WebClientConfig2;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
public class KakaoWebclient {
    @Value("${extsys.kakao.apikey}")
    private String apiKey;

    @Value("${extsys.kakao.url}")
    private String baseUrl;

    @Bean(value = "kakaoClient")
    public WebClient kakaoClient() {

        HttpClient httpClient =  HttpClient.create(this.getConntectionProvider())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(3))
                                .addHandlerLast(new WriteTimeoutHandler(5)));

        WebClient client = WebClient.builder()
                .defaultHeader("Authorization", new StringBuffer().append("KakaoAK").append(" ").append(apiKey).toString())
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        return client;

    }
    public ConnectionProvider getConntectionProvider() {
        return ConnectionProvider.builder("http-pool")
                .maxConnections(100)
                .pendingAcquireTimeout(Duration.ofMillis(0))
                .pendingAcquireMaxCount(-1)
                .maxIdleTime(Duration.ofMillis(1000L))
                .build();
    }
}
