package org.zephyrsoft.wab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zephyrsoft.wab.model.Family;
import org.zephyrsoft.wab.model.Person;

public interface FamilyRepository extends JpaRepository<Family, Long> {
    List<Family> findAllByOrderByLastNameAscIdAsc();

    Family findByMembersContaining(Person member);
}
