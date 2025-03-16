package org.zephyrsoft.wab.repository;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zephyrsoft.wab.model.Family;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FamilyRepositoryIT {
    @Autowired
    private FamilyRepository familyRepository;

    @Test
    void familyLoadedCompletely() {
        List<Family> families = familyRepository.findAll();

        assertThat(families).hasSize(8);
        assertThat(families.getFirst().getMembers()).hasSize(4);
    }
}
