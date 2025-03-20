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

    @Test
    void familyIdGenerated() {
        Family f1 = new Family();
        assertThat(f1.getId()).isNull();
        Family f2 = familyRepository.save(f1);
        assertThat(f1.getId()).isNotNull();
        assertThat(f1.getId()).isEqualTo(f2.getId());
    }
}
