package org.zephyrsoft.wab.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zephyrsoft.wab.report.Report;
import org.zephyrsoft.wab.repository.FamilyRepository;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class ReportServiceIT {
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private ReportService reportService;

    @Test
    void reportIsCreated() {
        Report report = reportService.exportAsPdf();

        assertThat(report.getContent()).isNotNull().isNotEmpty();
        assertThat(report.getFilename()).isNotEmpty();
        log.info("report with filename {} had {} bytes ({} kB) and contained {} families",
            report.getFilename(), report.getContent().length, report.getContent().length / 1024,
            familyRepository.findAll().size());
    }

    @Test
    void createHtml() {
        String html = reportService.createHtml();

        assertThat(html).contains("<h1><u>Address Book</u></h1>Here you can find the addresses as of");
    }
}
