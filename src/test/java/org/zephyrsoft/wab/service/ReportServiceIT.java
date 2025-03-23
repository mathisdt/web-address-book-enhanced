package org.zephyrsoft.wab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zephyrsoft.wab.repository.FamilyRepository;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
public class ReportServiceIT {
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private ReportService reportService;

    @Test
    void reportIsCreated() {
        byte[] bytes = reportService.exportAsPdf();

        assertThat(bytes).isNotNull().isNotEmpty();
        log.info("report had {} bytes ({} kB) and contained {} families",
            bytes.length, bytes.length / 1024, familyRepository.findAll().size());
    }

    @Test
    void createHtml() {
        String html = reportService.createHtml();

        assertThat(html).contains("<h1><u>Address Book</u></h1>Here you can find the addresses as of");
    }
}
