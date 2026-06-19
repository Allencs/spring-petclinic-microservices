package org.springframework.samples.petclinic.vets.web;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.vets.model.Vet;
import org.springframework.samples.petclinic.vets.model.VetRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Fourth hop of the owner report chain: returns vet data and delegates to
 * genai-service to build the final summary.
 */
@RestController
class VetReportResource {

    private static final Logger log = LoggerFactory.getLogger(VetReportResource.class);

    private final VetRepository vetRepository;

    private final GenAiReportClient genAiReportClient;

    VetReportResource(VetRepository vetRepository, GenAiReportClient genAiReportClient) {
        this.vetRepository = vetRepository;
        this.genAiReportClient = genAiReportClient;
    }

    @GetMapping("/vets/report")
    public Map<String, Object> vetsReport() {
        List<Vet> vets = vetRepository.findAll();
        log.info("Building vets report with {} vets", vets.size());

        Map<String, Object> summaryRequest = new LinkedHashMap<>();
        summaryRequest.put("source", "vets-service");
        summaryRequest.put("vetCount", vets.size());

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("service", "vets-service");
        report.put("generatedAt", Instant.now().toString());
        report.put("vetCount", vets.size());
        report.put("summary", genAiReportClient.summarize(summaryRequest));
        return report;
    }
}
