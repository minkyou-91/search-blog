package com.searchblog.global.infra;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Component
public class WebClientConfig {

    public WebClientConfig() {

    }
    public WebClient getWebClient() {
        return WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .clientConnector(new ReactorClientHttpConnector(this.getHttpClient()))
                .build();
    }


    protected HttpClient getHttpClient() {
        return HttpClient.create(this.connectionProvider())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) // httpclient 총 연결시간 10초
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5))
                                .addHandlerLast(new WriteTimeoutHandler(10)));
    }

    /**
     * <pre>
     * connection pool의 갯수
     * 커넥션 풀에서 커넥션을 얻기 위해 기다리는 최대 시간
     * 커넥션 풀에서 커넥션을 가져오는 시도 횟수 (-1: no limit)
     * 커넥션 풀에서 idle 상태의 커넥션을 유지하는 시간
     * </pre>
     */
     public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("http-pool")
                .maxConnections(100)
                .pendingAcquireTimeout(Duration.ofMillis(0))
                .pendingAcquireMaxCount(-1)
                .maxIdleTime(Duration.ofMillis(1000L))
                .build();
    }
}
