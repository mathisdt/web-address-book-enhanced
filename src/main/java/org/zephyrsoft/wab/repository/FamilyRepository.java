package org.zephyrsoft.wab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zephyrsoft.wab.model.Family;

public interface FamilyRepository extends JpaRepository<Family, Long> {
}
