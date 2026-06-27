package org.springframework.samples.petclinic.customers.web;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Provides a load-balanced {@link RestTemplate} used by the report call chain so that
 * service-to-service calls are resolved through Eureka by service id.
 *
 * <p>The template is built from the auto-configured {@link RestTemplateBuilder} so that
 * Micrometer's {@code ObservationRestTemplateCustomizer} is applied. That instruments the
 * call (creating a client span) and propagates the trace context (B3 headers) downstream.
 */
@Configuration
class ReportClientConfig {

    @Bean
    @LoadBalanced
    RestTemplate reportRestTemplate(RestTemplateBuilder builder,
            @Value("${petclinic.client.connect-timeout:1s}") Duration connectTimeout,
            @Value("${petclinic.client.read-timeout:2s}") Duration readTimeout) {
        return builder
            .connectTimeout(connectTimeout)
            .readTimeout(readTimeout)
            .build();
    }
}
