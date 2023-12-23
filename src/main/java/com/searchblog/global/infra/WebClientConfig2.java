package com.searchblog.global.infra;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.Builder;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

public class WebClientConfig2 {

    public WebClient getWebClient() {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(
                        client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000) //miliseconds
                                .doOnConnected(
                                        conn -> conn.addHandlerLast(new ReadTimeoutHandler(5))  //sec
                                                .addHandlerLast(new WriteTimeoutHandler(60)) //sec
                                )
                );

        WebClient client = WebClient.builder()
//                .baseUrl("http://localhost:5011")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        return client;
    }
}
