package com.example.licensingservice.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ApiCaller {

    private static final Logger logger = LoggerFactory.getLogger(ApiCaller.class);

    private WebClient webClient;

    @Autowired
    public ApiCaller(final WebClient webClient){
        this.webClient = webClient;
    }

    /**
     * Synchronous Get Call to the given api
     * @param responseType expected response type from the called api
     * @param url the url to be called
     * @param <T> generic return type
     * @return
     */
    public <T> T syncGet(ParameterizedTypeReference<T> responseType, String url){
        return this.webClient
                .get().uri(url)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value()!=200, ClientResponse::createException)
                .bodyToMono(responseType)
                .onErrorResume(throwable ->
                    Mono.error(throwable))
                .block();
    }
}
