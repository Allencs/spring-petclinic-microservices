package org.springframework.samples.petclinic.genai;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Tail of the long "owner report" call chain
 * (api-gateway -> customers -> visits -> vets -> genai).
 * Produces a deterministic summary so the chain stays healthy even when no LLM
 * API key is configured.
 */
@RestController
public class ReportSummaryController {

    private static final Logger LOG = LoggerFactory.getLogger(ReportSummaryController.class);

    @PostMapping("/report-summary")
    public Map<String, Object> summarize(@RequestBody(required = false) Map<String, Object> payload) {
        LOG.info("Generating report summary for payload: {}", payload);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("service", "genai-service");
        result.put("generatedAt", Instant.now().toString());
        result.put("summary", buildSummary(payload));
        return result;
    }

    private String buildSummary(Map<String, Object> payload) {
        if (payload == null || payload.isEmpty()) {
            return "No upstream data provided to summarize.";
        }
        Object vetCount = payload.get("vetCount");
        return "Pet clinic report aggregated across owner, visit and veterinarian services"
            + (vetCount != null ? ", covering " + vetCount + " veterinarian(s)" : "") + ".";
    }
}
