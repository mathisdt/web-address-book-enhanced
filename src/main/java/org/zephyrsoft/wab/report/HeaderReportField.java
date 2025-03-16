package org.zephyrsoft.wab.report;

import java.util.stream.Stream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HeaderReportField {
    DATE("date");

    private final String name;

    public static HeaderReportField ofName(String name) {
        return Stream.of(values())
            .filter(hrf -> hrf.name.equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("HeaderReportField with name " + name + " unknown"));
    }
}
