package org.springframework.samples.petclinic.customers.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Calls visits-service as the next hop of the owner report chain.
 *
 * <p>Uses a load-balanced {@link RestTemplate} (resolved via Eureka by service id)
 * so that the call is traced by the SkyWalking RestTemplate plugin and the trace
 * context is propagated downstream.
 */
@Component
class VisitsReportClient {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE =
        new ParameterizedTypeReference<>() {
        };

    private final RestTemplate restTemplate;

    VisitsReportClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    Map<String, Object> getVisitsReport(List<Integer> petIds) {
        String url = "http://visits-service/pets/visits/report";
        if (!petIds.isEmpty()) {
            url += "?petId=" + petIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        }
        return restTemplate.exchange(url, HttpMethod.GET, null, MAP_TYPE).getBody();
    }
}
