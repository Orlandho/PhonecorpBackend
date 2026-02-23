package com.phonecorp.phonecorpbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Provee clientes HTTP configurados e inyectables para servicios externos.
 * El bean RestTemplate se inyecta en ReniecService y SunatService
 * cuando se implemente la integracion real con los APIs externos.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5_000);   // 5 segundos para establecer conexion
        factory.setReadTimeout(10_000);     // 10 segundos para leer la respuesta
        return new RestTemplate(factory);
    }
}