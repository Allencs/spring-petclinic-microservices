package org.springframework.samples.petclinic.visits.web;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.visits.model.Visit;
import org.springframework.samples.petclinic.visits.model.VisitRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Third hop of the owner report chain: returns visit data and delegates to
 * vets-service for the next hop.
 */
@RestController
class VisitReportResource {

    private static final Logger log = LoggerFactory.getLogger(VisitReportResource.class);

    private final VisitRepository visitRepository;

    private final VetsReportClient vetsReportClient;

    VisitReportResource(VisitRepository visitRepository, VetsReportClient vetsReportClient) {
        this.visitRepository = visitRepository;
        this.vetsReportClient = vetsReportClient;
    }

    @GetMapping("pets/visits/report")
    public Map<String, Object> visitsReport(@RequestParam(name = "petId", required = false) List<Integer> petIds) {
        List<Integer> ids = petIds == null ? Collections.emptyList() : petIds;
        List<Visit> visits = ids.isEmpty() ? Collections.emptyList() : visitRepository.findByPetIdIn(ids);
        log.info("Building visits report for petIds={} ({} visits)", ids, visits.size());

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("service", "visits-service");
        report.put("generatedAt", Instant.now().toString());
        report.put("petIds", ids);
        report.put("visitCount", visits.size());
        report.put("vetsReport", vetsReportClient.getVetsReport());
        return report;
    }
}
