package com.searchblog.global.exception;

import com.searchblog.global.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.net.URI;

@Slf4j
@Setter
@Getter
public class CustomWebClientRequestException extends WebClientRequestException {

    private final ErrorCode errorCode;
    private ClientResponse response;
    private URI uri;
    private Object data;
    public CustomWebClientRequestException(ClientResponse response, HttpMethod method, URI uri, Object data) {
        super(new RuntimeException(String.format("WebClient Request Error. [status: %d] [message: %s] [responseBody: %s]",
                        response.statusCode().value(), HttpStatus.resolve(response.rawStatusCode()).getReasonPhrase(), data.toString())),
                method, uri, response.headers().asHttpHeaders());
        log.error(super.getMessage());
        this.errorCode=ErrorCode.getErrorCode(response.statusCode());
        this.response=response;
        this.uri=uri;
        this.data = data;
    }
}
