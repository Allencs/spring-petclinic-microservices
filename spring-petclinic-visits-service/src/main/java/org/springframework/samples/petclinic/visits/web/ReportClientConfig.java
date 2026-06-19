package org.springframework.samples.petclinic.visits.web;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Provides a load-balanced {@link RestTemplate} used by the report call chain so
 * that service-to-service calls are resolved through Eureka and traced by the
 * SkyWalking agent.
 */
@Configuration
class ReportClientConfig {

    @Bean
    @LoadBalanced
    RestTemplate reportRestTemplate() {
        return new RestTemplate();
    }
}
