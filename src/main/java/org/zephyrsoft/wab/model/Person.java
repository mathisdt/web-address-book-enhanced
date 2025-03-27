package org.zephyrsoft.wab.model;

import java.sql.Timestamp;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import static java.util.stream.Collectors.joining;

/**
 * person data bean (a person is always member of a family)
 */
@Getter
@Setter
@Entity
@Table(name = "person")
public class Person implements HasContacts, ComparableBean<Person> {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "person_gen")
	@TableGenerator(
		name = "person_gen",
		table = "id_gen",
		pkColumnName = "gen_key",
		valueColumnName = "gen_value",
		pkColumnValue = "person",
		allocationSize = 1
	)
	private Integer id;
	@JsonIgnore
	@Version
	private Timestamp lastUpdate;

	private String firstName;
	private String lastName;
	private String birthday;
	private String contact1;
	private String contact2;
	private String contact3;
	private String remarks;
	private Integer ordering;

	public String getName() {
		return Stream.of(firstName, lastName)
			.filter(StringUtils::isNotEmpty)
			.collect(joining(" "));
	}

	@Override
	public int compareTo(final Person o) {
		if (o == null) {
			return -1;
		}
		int ret = INTEGER_NULLS_LAST.compare(ordering, o.ordering);
		if (ret == 0) {
			ret = STRING_NULLS_LAST.compare(firstName, o.firstName);
		}
		return ret;
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof Person p) {
			return 0 == compareTo(p);
		} else {
			return super.equals(o);
		}
	}

	@Override
	public int hashCode() {
		// nothing special required, use superclass implementation
		return super.hashCode();
	}

}
