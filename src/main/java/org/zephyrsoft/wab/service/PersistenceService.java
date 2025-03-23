package org.zephyrsoft.wab.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.zephyrsoft.wab.mapper.FamilyMapper;
import org.zephyrsoft.wab.mapper.PersonMapper;
import org.zephyrsoft.wab.model.Family;
import org.zephyrsoft.wab.model.Person;
import org.zephyrsoft.wab.repository.FamilyRepository;
import org.zephyrsoft.wab.repository.PersonRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersistenceService {
    private final FamilyRepository familyRepository;
    private final PersonRepository personRepository;
    private final FamilyMapper familyMapper;
    private final PersonMapper personMapper;

    public List<Family> readAllFamilies() {
        return familyRepository.findAllByOrderByLastNameAscIdAsc();
    }

    public Family readFamily(@PathVariable Long familyId) {
        Optional<Family> found = familyRepository.findById(familyId);
        if (found.isEmpty()) {
            throw new EntityNotFoundException("no family with ID " + familyId + " found");
        }
        return found.get();
    }

    public Person readPerson(@PathVariable Long personId) {
        Optional<Person> found = personRepository.findById(personId);
        if (found.isEmpty()) {
            throw new EntityNotFoundException("no person with ID " + personId + " found");
        }
        return found.get();
    }

    public Family createFamily(@RequestBody(required = false) Family family) {
        Family familyToCreate = family != null
            ? family
            : new Family();
        familyToCreate.setId(null);
        return familyRepository.save(familyToCreate);
    }

    public Family updateFamily(@PathVariable Long familyId, @RequestBody Family family) {
        if (familyId < 0) {
            throw new IllegalArgumentException("family ID " + familyId + " invalid");
        }
        Optional<Family> found = familyRepository.findById(familyId);
        if (found.isEmpty()) {
            throw new EntityNotFoundException("no family with ID " + familyId + " found");
        }
        familyMapper.mapWithoutId(family, found.get());
        return familyRepository.save(found.get());
    }

    public void deleteFamily(@PathVariable Long familyId) {
        if (familyId < 0) {
            throw new IllegalArgumentException("family ID " + familyId + " invalid");
        }
        Optional<Family> found = familyRepository.findById(familyId);
        if (found.isEmpty()) {
            throw new EntityNotFoundException("no family with ID " + familyId + " found");
        }
        familyRepository.delete(found.get());
    }

    public Person createPersonForFamily(@PathVariable Long familyId) {
        if (familyId < 0) {
            throw new IllegalArgumentException("family ID " + familyId + " invalid");
        }
        Optional<Family> found = familyRepository.findById(familyId);
        if (found.isEmpty()) {
            throw new EntityNotFoundException("no family with ID " + familyId + " found");
        }
        Person person = found.get().addNewMember();
        return personRepository.save(person);
    }

    public Person updatePerson(@PathVariable Long personId, @RequestBody Person person) {
        if (personId < 0) {
            throw new IllegalArgumentException("person ID " + personId + " invalid");
        }
        Optional<Person> found = personRepository.findById(personId);
        if (found.isEmpty()) {
            throw new EntityNotFoundException("no person with ID " + personId + " found");
        }
        personMapper.mapWithoutId(person, found.get());
        return personRepository.save(found.get());
    }

    public void deletePerson(@PathVariable Long personId) {
        if (personId < 0) {
            throw new IllegalArgumentException("person ID " + personId + " invalid");
        }
        Optional<Person> found = personRepository.findById(personId);
        if (found.isEmpty()) {
            throw new EntityNotFoundException("no person with ID " + personId + " found");
        }
        found.get().getFamily().removeMember(found.get());
        familyRepository.save(found.get().getFamily());
        personRepository.delete(found.get());
    }

    public Family movePersonUp(@PathVariable Long personId) {
        if (personId < 0) {
            throw new IllegalArgumentException("person ID " + personId + " invalid");
        }
        Optional<Person> found = personRepository.findById(personId);
        if (found.isEmpty()) {
            throw new EntityNotFoundException("no person with ID " + personId + " found");
        }
        if (!found.get().getFamily().mayMoveUp(found.get())) {
            throw new UnsupportedOperationException("person " + personId + " can't move up");
        }
        found.get().getFamily().moveUp(found.get());
        familyRepository.save(found.get().getFamily());
        return found.get().getFamily();
    }

    public Family movePersonDown(@PathVariable Long personId) {
        if (personId < 0) {
            throw new IllegalArgumentException("person ID " + personId + " invalid");
        }
        Optional<Person> found = personRepository.findById(personId);
        if (found.isEmpty()) {
            throw new EntityNotFoundException("no person with ID " + personId + " found");
        }
        if (!found.get().getFamily().mayMoveDown(found.get())) {
            throw new UnsupportedOperationException("person " + personId + " can't move down");
        }
        found.get().getFamily().moveDown(found.get());
        familyRepository.save(found.get().getFamily());
        return found.get().getFamily();
    }
}
