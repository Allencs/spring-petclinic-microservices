package org.springframework.samples.petclinic.vets.web;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Calls genai-service to obtain the final summary of the owner report chain.
 *
 * <p>Uses a load-balanced {@link RestTemplate} so the call is traced by the
 * SkyWalking RestTemplate plugin and the trace context is propagated downstream.
 */
@Component
class GenAiReportClient {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE =
        new ParameterizedTypeReference<>() {
        };

    private final RestTemplate restTemplate;

    GenAiReportClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    Map<String, Object> summarize(Map<String, Object> payload) {
        return restTemplate
            .exchange("http://genai-service/report-summary", HttpMethod.POST, new HttpEntity<>(payload), MAP_TYPE)
            .getBody();
    }
}
