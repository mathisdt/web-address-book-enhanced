package org.zephyrsoft.wab.model;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import static java.util.stream.Collectors.joining;

public interface HasContacts {

	String getContact1();

	String getContact2();

	String getContact3();

	default String getContacts() {
		return Stream.of(getContact1(), getContact2(), getContact3())
			.filter(StringUtils::isNotEmpty)
			.collect(joining(" / "));
	}
}
