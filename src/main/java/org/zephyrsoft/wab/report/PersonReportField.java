package org.zephyrsoft.wab.report;

import java.util.stream.Stream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PersonReportField {
    NAME("name"),
    BIRTHDAY("birthday"),
    CONTACTS("contacts");

    private final String name;

    public static PersonReportField ofName(String name) {
        return Stream.of(values())
            .filter(prf -> prf.name.equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("PersonReportField with name " + name + " unknown"));
    }
}
