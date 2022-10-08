package com.example.apigateway.filter;


import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.EntityResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * @explanation response edit
 * // todo fix the class
 */

public class ResponseUtil {
    @NotNull
    public static <T> Mono<Void> putResponseIntoWebExchange(ServerWebExchange exchange,
                                                            ServerCodecConfigurer serverCodecConfigurer,
                                                            Mono<EntityResponse<T>> responseMono) {

        return responseMono.flatMap(entityResponse ->
                entityResponse.writeTo(exchange, new ServerResponse.Context() {
                    @NotNull
                    @Override
                    public List<HttpMessageWriter<?>> messageWriters() {
                        return serverCodecConfigurer.getWriters();
                    }

                    @NotNull
                    @Override
                    public List<ViewResolver> viewResolvers() {
                        return Collections.emptyList();
                    }
                }));
    }


}