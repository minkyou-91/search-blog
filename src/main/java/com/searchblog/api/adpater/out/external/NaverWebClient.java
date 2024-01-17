package com.searchblog.api.adpater.out.external;

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
public class NaverWebClient {
    @Value("${extsys.naver.url}")
    private String baseUrl;
    @Value("${extsys.naver.client.id}")
    private String clientId;
    @Value("${extsys.naver.client.secret}")
    private String clientSecret;

    @Bean(value = "naverClient")
    public WebClient naverClient() {
        HttpClient httpClient = HttpClient.create(this.getConntectionProvider())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(3))
                                .addHandlerLast(new WriteTimeoutHandler(5)));

        WebClient client = WebClient.builder()
                .defaultHeaders(header -> {
                    header.set("X-Naver-Client-Id", clientId);
                    header.set("X-Naver-Client-Secret", clientSecret);
                })
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
