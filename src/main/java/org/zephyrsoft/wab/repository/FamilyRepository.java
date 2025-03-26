package org.zephyrsoft.wab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zephyrsoft.wab.model.Family;

public interface FamilyRepository extends JpaRepository<Family, Long> {
    @EntityGraph(attributePaths = Family.Fields.members)
    List<Family> findAllByOrderByLastNameAscIdAsc();
}
