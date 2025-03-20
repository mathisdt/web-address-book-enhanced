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

        assertThat(html).contains("""
                <html><head><title>Address Book</title><style>
                @page { margin-bottom: 20mm; @bottom-center { content: counter(page) " / " counter(pages) } }
                </style></head><body>
                <h1><u>Address Book</u></h1>Here you can find the addresses as of 20/03/2025.<br/><br/>
                """)
            .contains("""
                <div style="page-break-inside: avoid; border-top:1px solid"><div style="display:inline-block; width: 20%; vertical-align:top; padding-top:3px; padding-bottom:3px; text-overflow: ellipsis">Family 4</div><div style="display:inline-block; vertical-align:top; padding-top:3px; padding-bottom:3px; width: 78%; line-break: anywhere">Street 4, 123 City 4</div><div style="margin-left: 12%; padding-bottom:3px; width: 86%; line-break: anywhere">0456-78901</div>
                <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 4-1</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">04.01.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere"></div></div>
                <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 4-2</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">04.02.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere"></div></div>
                </div>""");
    }
}
