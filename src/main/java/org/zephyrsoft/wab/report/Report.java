package org.zephyrsoft.wab.report;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Report {
    private String filename;
    private byte[] content;
}
