package org.zephyrsoft.wab.repository;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zephyrsoft.wab.model.Person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PersonRepositoryIT {
    @Autowired
    private PersonRepository personRepository;

    @Test
    void personLoadedCompletely() {
        Optional<Person> person = personRepository.findById(1L);

        assertTrue(person.isPresent());
        assertThat(person.get().getFamily()).isNotNull();
        assertThat(person.get().getFamily().getMembers()).hasSize(4);
    }
}
