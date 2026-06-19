package org.springframework.samples.petclinic.visits.web;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Calls vets-service as the next hop of the owner report chain.
 *
 * <p>Uses a load-balanced {@link RestTemplate} so the call is traced by the
 * SkyWalking RestTemplate plugin and the trace context is propagated downstream.
 */
@Component
class VetsReportClient {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE =
        new ParameterizedTypeReference<>() {
        };

    private final RestTemplate restTemplate;

    VetsReportClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    Map<String, Object> getVetsReport() {
        return restTemplate
            .exchange("http://vets-service/vets/report", HttpMethod.GET, null, MAP_TYPE)
            .getBody();
    }
}
