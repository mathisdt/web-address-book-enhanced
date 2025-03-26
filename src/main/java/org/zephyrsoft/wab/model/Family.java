package org.zephyrsoft.wab.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * family data bean
 */
@Getter
@Setter
@FieldNameConstants
@Entity
@Table(name = "family")
public class Family implements HasContacts, ComparableBean<Family> {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "family_gen")
	@TableGenerator(
		name = "family_gen",
		table = "id_gen",
		pkColumnName = "gen_key",
		valueColumnName = "gen_value",
		pkColumnValue = "family",
		allocationSize = 1
	)
	private Integer id;
	@JsonIgnore
	@Version
	private Timestamp lastUpdate;

	private String lastName;
	private String street;
	private String postalCode;
	private String city;
	private String contact1;
	private String contact2;
	private String contact3;
	private String remarks;

	@OrderBy("ordering, firstName")
	@OneToMany(targetEntity = Person.class, mappedBy = "family", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Person> members;

	private void initMembersIfNecessary() {
		if (members == null) {
			members = new ArrayList<>();
		}
	}

	private void setMemberOrdering() {
		initMembersIfNecessary();
		for (int i = 0; i < members.size(); i++) {
			Person p = members.get(i);
			p.setOrdering(i);
		}
	}

	public List<Person> getMembers() {
		initMembersIfNecessary();
		return Collections.unmodifiableList(members);
	}

	public Person addNewMember() {
		initMembersIfNecessary();
		Person newPerson = new Person();
		members.add(newPerson);
		newPerson.setFamily(this);
		setMemberOrdering();
		return newPerson;
	}

	public boolean containsMember(final Person o) {
		initMembersIfNecessary();
		return members.contains(o);
	}

	public void removeMember(final Person o) {
		initMembersIfNecessary();
		members.remove(o);
		setMemberOrdering();
	}

	public boolean mayMoveUp(final Person p) {
		initMembersIfNecessary();
		// person is in list, but not at first position
		return containsMember(p) && members != null && members.indexOf(p) > 0;
	}

	public boolean mayMoveDown(final Person p) {
		initMembersIfNecessary();
		// person is in list, but not at last position
		return containsMember(p) && members != null && members.lastIndexOf(p) < members.size() - 1;
	}

	public void moveUp(final Person p) {
		initMembersIfNecessary();
		if (mayMoveUp(p)) {
			int sourceIndex = members.indexOf(p);
			int targetIndex = sourceIndex - 1;
			Person toSwitch = members.get(targetIndex);
			members.set(targetIndex, p);
			members.set(sourceIndex, toSwitch);
			setMemberOrdering();
		}
	}

	public void moveDown(final Person p) {
		initMembersIfNecessary();
		if (mayMoveDown(p)) {
			int sourceIndex = members.lastIndexOf(p);
			int targetIndex = sourceIndex + 1;
			Person toSwitch = members.get(targetIndex);
			members.set(targetIndex, p);
			members.set(sourceIndex, toSwitch);
			setMemberOrdering();
		}
	}

	public String getAddress() {
		String ret = "";
		if (StringUtils.isNotEmpty(street)) {
			ret = street;
		}
		if (StringUtils.isNotEmpty(ret)
			&& (StringUtils.isNotEmpty(postalCode) || StringUtils.isNotEmpty(city))) {
			ret += ", ";
		}
		if (StringUtils.isNotEmpty(postalCode)) {
			ret += postalCode;
		}
		if (StringUtils.isNotEmpty(postalCode) && StringUtils.isNotEmpty(city)) {
			ret += " ";
		}
		if (StringUtils.isNotEmpty(city)) {
			ret += city;
		}
		return ret;
	}

	/**
	 * Compare this family to another. This is done by comparing the last names and
	 * then the street and then the contact fields (null values are always last).
	 */
	@Override
	public int compareTo(final Family o) {
		// other object is null => other object is larger than this
		if (o == null) {
			return -1;
		}
		// now compare by lastName, street, contact1, contact2, contact3
        int returnValue = STRING_NULLS_LAST.compare(lastName, o.lastName);
        if (returnValue == 0) {
			returnValue = STRING_NULLS_LAST.compare(street, o.street);
		}
		if (returnValue == 0) {
			returnValue = STRING_NULLS_LAST.compare(contact1, o.contact1);
		}
		if (returnValue == 0) {
			returnValue = STRING_NULLS_LAST.compare(contact2, o.contact2);
		}
		if (returnValue == 0) {
			returnValue = STRING_NULLS_LAST.compare(contact3, o.contact3);
		}
		return returnValue;
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof Family f) {
			return 0 == compareTo(f);
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
