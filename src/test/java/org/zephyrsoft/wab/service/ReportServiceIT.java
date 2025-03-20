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

        assertEquals("""
            <html><head><title>Address Book</title><style>
            @page { margin-bottom: 20mm; @bottom-center { content: counter(page) " / " counter(pages) } }
            </style></head><body>
            <h1><u>Address Book</u></h1>Here you can find the addresses as of 20/03/2025.<br/><br/>
            <div style="page-break-inside: avoid; border-top:1px solid"><div style="display:inline-block; width: 20%; vertical-align:top; padding-top:3px; padding-bottom:3px; text-overflow: ellipsis">Family 1</div><div style="display:inline-block; vertical-align:top; padding-top:3px; padding-bottom:3px; width: 78%; line-break: anywhere">Street 1, 123 City 1</div><div style="margin-left: 12%; padding-bottom:3px; width: 86%; line-break: anywhere">0123-45678</div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 1-1</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">01.01.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">one1@example.com</div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 1-2</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">01.02.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">one2@example.com</div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 1-3</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">01.03.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">one3@example.com</div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 1-4</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">01.04.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">one4@example.com</div></div>
            </div>
            <div style="page-break-inside: avoid; border-top:1px solid"><div style="display:inline-block; width: 20%; vertical-align:top; padding-top:3px; padding-bottom:3px; text-overflow: ellipsis">Family 2</div><div style="display:inline-block; vertical-align:top; padding-top:3px; padding-bottom:3px; width: 78%; line-break: anywhere">Street 2, 123 City 2</div><div style="margin-left: 12%; padding-bottom:3px; width: 86%; line-break: anywhere">0234-56789</div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 2</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">02.01.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">two@example.com</div></div>
            </div>
            <div style="page-break-inside: avoid; border-top:1px solid"><div style="display:inline-block; width: 20%; vertical-align:top; padding-top:3px; padding-bottom:3px; text-overflow: ellipsis">Family 3</div><div style="display:inline-block; vertical-align:top; padding-top:3px; padding-bottom:3px; width: 78%; line-break: anywhere">Street 3, 123 City 3</div><div style="margin-left: 12%; padding-bottom:3px; width: 86%; line-break: anywhere">0345-67890</div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 3-1</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">03.01.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">three1@example.com</div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 3-2</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">03.02.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">three2@example.com</div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 3-3</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">03.03.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">three3@example.com</div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 3-4</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">03.04.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">three4@example.com</div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 3-5</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">03.05.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">three5@example.com</div></div>
            </div>
            <div style="page-break-inside: avoid; border-top:1px solid"><div style="display:inline-block; width: 20%; vertical-align:top; padding-top:3px; padding-bottom:3px; text-overflow: ellipsis">Family 4</div><div style="display:inline-block; vertical-align:top; padding-top:3px; padding-bottom:3px; width: 78%; line-break: anywhere">Street 4, 123 City 4</div><div style="margin-left: 12%; padding-bottom:3px; width: 86%; line-break: anywhere">0456-78901</div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 4-1</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">04.01.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere"></div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 4-2</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">04.02.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere"></div></div>
            </div>
            <div style="page-break-inside: avoid; border-top:1px solid"><div style="display:inline-block; width: 20%; vertical-align:top; padding-top:3px; padding-bottom:3px; text-overflow: ellipsis">Family 5</div><div style="display:inline-block; vertical-align:top; padding-top:3px; padding-bottom:3px; width: 78%; line-break: anywhere">Street 5, 123 City 5</div><div style="margin-left: 12%; padding-bottom:3px; width: 86%; line-break: anywhere">0123-45678</div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 5-1</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">05.01.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">one1@example.com</div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 5-2</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">05.02.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">one2@example.com</div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 5-3</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">05.03.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">one3@example.com</div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 5-4</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">05.04.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">one4@example.com</div></div>
            </div>
            <div style="page-break-inside: avoid; border-top:1px solid"><div style="display:inline-block; width: 20%; vertical-align:top; padding-top:3px; padding-bottom:3px; text-overflow: ellipsis">Family 6</div><div style="display:inline-block; vertical-align:top; padding-top:3px; padding-bottom:3px; width: 78%; line-break: anywhere">Street 6, 123 City 6</div><div style="margin-left: 12%; padding-bottom:3px; width: 86%; line-break: anywhere">0234-56789</div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 6</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">06.01.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">two@example.com</div></div>
            </div>
            <div style="page-break-inside: avoid; border-top:1px solid"><div style="display:inline-block; width: 20%; vertical-align:top; padding-top:3px; padding-bottom:3px; text-overflow: ellipsis">Family 7</div><div style="display:inline-block; vertical-align:top; padding-top:3px; padding-bottom:3px; width: 78%; line-break: anywhere">Street 7, 123 City 7</div><div style="margin-left: 12%; padding-bottom:3px; width: 86%; line-break: anywhere">0345-67890</div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 7-1</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">07.01.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">three1@example.com</div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 7-2</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">07.02.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">three2@example.com</div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 7-3</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">07.03.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">three3@example.com</div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 7-4</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">07.04.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">three4@example.com</div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 7-5</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">07.05.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere">three5@example.com</div></div>
            </div>
            <div style="page-break-inside: avoid; border-top:1px solid"><div style="display:inline-block; width: 20%; vertical-align:top; padding-top:3px; padding-bottom:3px; text-overflow: ellipsis">Family 8</div><div style="display:inline-block; vertical-align:top; padding-top:3px; padding-bottom:3px; width: 78%; line-break: anywhere">Street 8, 123 City 8</div><div style="margin-left: 12%; padding-bottom:3px; width: 86%; line-break: anywhere">0456-78901</div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 8-1</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">08.01.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere"></div></div>
            <div style="margin-left: 12%; border-top:1px solid; padding-top:3px; padding-bottom:3px"><div style="display:inline-block; width: 20%; vertical-align:top">Surname 8-2</div> <div style="display:inline-block; width: 13%; vertical-align:top; line-break: anywhere">08.02.</div><div style="display:inline-block; vertical-align:top; width: 63%; line-break: anywhere"></div></div>
            </div>
            </body></html>""", html);
    }
}
