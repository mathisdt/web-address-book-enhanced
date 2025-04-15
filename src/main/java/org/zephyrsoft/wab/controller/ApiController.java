package org.zephyrsoft.wab.controller;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.zephyrsoft.wab.model.Family;
import org.zephyrsoft.wab.model.Person;
import org.zephyrsoft.wab.report.Report;
import org.zephyrsoft.wab.service.PersistenceService;
import org.zephyrsoft.wab.service.ReportService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/data")
public class ApiController {
    private final PersistenceService persistenceService;
    private final ReportService reportService;

    @GetMapping("/family")
    public List<Family> readAllFamilies() {
        return persistenceService.readAllFamilies();
    }

    @GetMapping("/family/{familyId}")
    public Family readFamily(@PathVariable Long familyId) {
        return persistenceService.readFamily(familyId);
    }

    @GetMapping("/person/{personId}")
    public Person readPerson(@PathVariable Long personId) {
        return persistenceService.readPerson(personId);
    }

    @PostMapping("/family")
    public Family createFamily(@RequestBody(required = false) Family family) {
        return persistenceService.createFamily(family);
    }

    @PutMapping("/family/{familyId}")
    public Family updateFamily(@PathVariable Long familyId, @RequestBody Family family) {
        return persistenceService.updateFamily(familyId, family);
    }

    @DeleteMapping("/family/{familyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFamily(@PathVariable Long familyId) {
        persistenceService.deleteFamily(familyId);
    }

    @PostMapping("/family/{familyId}")
    public Person createPersonForFamily(@PathVariable Long familyId) {
        return persistenceService.createPersonForFamily(familyId);
    }

    @PutMapping("/person/{personId}")
    public Person updatePerson(@PathVariable Long personId, @RequestBody Person person) {
        return persistenceService.updatePerson(personId, person);
    }

    @DeleteMapping("/person/{personId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable Long personId) {
        persistenceService.deletePerson(personId);
    }

    @PutMapping("/person/{personId}/moveUp")
    public Family movePersonUp(@PathVariable Long personId) {
        return persistenceService.movePersonUp(personId);
    }

    @PutMapping("/person/{personId}/moveDown")
    public Family movePersonDown(@PathVariable Long personId) {
        return persistenceService.movePersonDown(personId);
    }

    @GetMapping(value = "/report", produces = MediaType.APPLICATION_PDF_VALUE)
    public HttpEntity<byte[]> createReport(HttpServletResponse response) {
        Report report = reportService.exportAsPdf();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + report.getFilename());

        return new HttpEntity<>(report.getContent(), headers);
    }
}
