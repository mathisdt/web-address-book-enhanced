package org.zephyrsoft.wab.repository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zephyrsoft.wab.model.Family;
import org.zephyrsoft.wab.model.Person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PersonRepositoryIT {
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private PersonRepository personRepository;

    @Test
    void personLoaded() {
        Optional<Person> person = personRepository.findById(1L);

        assertTrue(person.isPresent());
    }

    @Test
    void personIdGenerated() {
        Optional<Family> f1 = familyRepository.findById(1L);
        assertThat(f1).isNotEmpty();
        Person p1 = f1.get().addNewMember();
        assertThat(p1.getId()).isNull();
        Person p2 = personRepository.save(p1);
        assertThat(p1.getId()).isNotNull();
        assertThat(p1.getId()).isEqualTo(p2.getId());
    }
}
