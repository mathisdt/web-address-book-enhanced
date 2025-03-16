package org.zephyrsoft.wab.model;

import java.util.Comparator;

/**
 * superclass for any data beans used in this applications
 */
@FunctionalInterface
public interface ComparableBean<T> extends Comparable<T> {
    Comparator<String> STRING_NULLS_LAST = Comparator.nullsLast(Comparator.naturalOrder());
    Comparator<Integer> INTEGER_NULLS_LAST = Comparator.nullsLast(Comparator.naturalOrder());

	@Override
    int compareTo(T o);
}
