package org.zephyrsoft.wab.report;

import java.util.stream.Stream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FamilyReportField {
    LAST_NAME("lastName"),
    ADDRESS("address"),
    CONTACTS("contacts"),
    PERSONS("persons");

    private final String name;

    public static FamilyReportField ofName(String name) {
        return Stream.of(values())
            .filter(frf -> frf.name.equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("FamilyReportField with name " + name + " unknown"));
    }
}
