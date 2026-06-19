package org.springframework.samples.petclinic.customers.web;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.customers.model.Owner;
import org.springframework.samples.petclinic.customers.model.OwnerRepository;
import org.springframework.samples.petclinic.customers.model.Pet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Second hop of the owner report chain: returns owner data and delegates to
 * visits-service for the next hop.
 */
@RestController
class ReportResource {

    private static final Logger log = LoggerFactory.getLogger(ReportResource.class);

    private final OwnerRepository ownerRepository;

    private final VisitsReportClient visitsReportClient;

    ReportResource(OwnerRepository ownerRepository, VisitsReportClient visitsReportClient) {
        this.ownerRepository = ownerRepository;
        this.visitsReportClient = visitsReportClient;
    }

    @GetMapping("/owners/{ownerId}/report")
    public Map<String, Object> ownerReport(@PathVariable("ownerId") int ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
            .orElseThrow(() -> new ResourceNotFoundException("Owner " + ownerId + " not found"));

        List<Integer> petIds = owner.getPets().stream().map(Pet::getId).toList();
        log.info("Building owner report for ownerId={} with petIds={}", ownerId, petIds);

        Map<String, Object> ownerData = new LinkedHashMap<>();
        ownerData.put("id", owner.getId());
        ownerData.put("firstName", owner.getFirstName());
        ownerData.put("lastName", owner.getLastName());
        ownerData.put("petIds", petIds);

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("service", "customers-service");
        report.put("generatedAt", Instant.now().toString());
        report.put("owner", ownerData);
        report.put("visitsReport", visitsReportClient.getVisitsReport(petIds));
        return report;
    }
}
