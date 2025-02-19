package com.procesos.colas.Beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

@Configuration
public class ResTemplateConfiguration {

    @Value("${alfresco.url}")
    private String baseUrl;

    @Value("${alfresco.auth.usuario}")
    private String usuario;

    @Value("${alfresco.auth.contrasena}")
    private String contrasena;

    private static final Logger log = LoggerFactory.getLogger(ResTemplateConfiguration.class);

    @Bean
    @Qualifier("alfresco")
    public RestTemplate restTemplateAlfresco() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(30000);
        factory.setReadTimeout(30000);

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));


        restTemplate.setInterceptors(Collections.singletonList(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {


                log.info("Request URL: " + request.getURI());
                log.info("Request Method: " + request.getMethod());
                log.info("Request Body: " + new String(body, StandardCharsets.UTF_8));
                log.info("Request Headers: " + request.getHeaders());

                String credentials = usuario + ":" + contrasena;
                byte[] encodedAuth = Base64.getEncoder().encode(credentials.getBytes(StandardCharsets.UTF_8));
                String authHeader = "Basic " + new String(encodedAuth);

                request.getHeaders().set("Authorization", authHeader);

                return execution.execute(request, body);
            }
        }));

        return restTemplate;
    }


}
