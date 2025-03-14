package org.zephyrsoft.wab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zephyrsoft.wab.model.Family;
import org.zephyrsoft.wab.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
