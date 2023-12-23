package com.searchblog.global.exception;

import com.searchblog.global.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
@Setter
@Getter
public class CustomWebClientResponseException extends WebClientResponseException {

    private ErrorCode errorCode;
    private ClientResponse response;
    private URI uri;
    private String message;
    private Object data;

    public CustomWebClientResponseException(ClientResponse response, HttpMethod method, URI uri, Object data) {
        super(String.format("WebClient Response Error. [status: %d] [message: %s]", response.statusCode().value(), HttpStatus.resolve(response.rawStatusCode()).getReasonPhrase()),
                response.statusCode().value(),
                HttpStatus.resolve(response.rawStatusCode()).getReasonPhrase(),
                response.headers().asHttpHeaders(),
                data.toString().getBytes(),
                StandardCharsets.UTF_8);

        log.error(super.getMessage());

        this.errorCode= ErrorCode.getErrorCode(response.statusCode());
        this.response=response;
        this.uri=uri;
        this.data=data;
    }
}
