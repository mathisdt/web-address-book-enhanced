package org.zephyrsoft.wab.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.zephyrsoft.wab.mapper.FamilyMapper;
import org.zephyrsoft.wab.mapper.PersonMapper;
import org.zephyrsoft.wab.model.Family;
import org.zephyrsoft.wab.model.Person;
import org.zephyrsoft.wab.repository.FamilyRepository;
import org.zephyrsoft.wab.repository.PersonRepository;
import org.zephyrsoft.wab.service.ReportService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/data")
public class ApiController {
    private final FamilyRepository familyRepository;
    private final PersonRepository personRepository;
    private final FamilyMapper familyMapper;
    private final PersonMapper personMapper;
    private final ReportService reportService;

    @GetMapping("/family")
    public List<Family> readAllFamilies() {
        return familyRepository.findAll();
    }

    @GetMapping("/family/{familyId}")
    public Family readFamily(@PathVariable Long familyId) {
        Optional<Family> found = familyRepository.findById(familyId);
        if (found.isEmpty()) {
            throw new EntityNotFoundException("no family with ID " + familyId + " found");
        }
        return found.get();
    }

    @GetMapping("/person/{personId}")
    public Person readPerson(@PathVariable Long personId) {
        Optional<Person> found = personRepository.findById(personId);
        if (found.isEmpty()) {
            throw new EntityNotFoundException("no person with ID " + personId + " found");
        }
        return found.get();
    }

    @PostMapping("/family")
    public Family createFamily(@RequestBody(required = false) Family family) {
        Family familyToCreate = family != null
            ? family
            : new Family();
        familyToCreate.setId(null);
        return familyRepository.save(familyToCreate);
    }

    @PutMapping("/family/{familyId}")
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

    @DeleteMapping("/family/{familyId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
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

    @PostMapping("/family/{familyId}")
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

    @PutMapping("/person/{personId}")
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

    @DeleteMapping("/person/{personId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable Long personId) {
        if (personId < 0) {
            throw new IllegalArgumentException("person ID " + personId + " invalid");
        }
        Optional<Person> found = personRepository.findById(personId);
        if (found.isEmpty()) {
            throw new EntityNotFoundException("no person with ID " + personId + " found");
        }
        personRepository.delete(found.get());
    }

    @PutMapping("/person/{personId}/moveUp")
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

    @PutMapping("/person/{personId}/moveDown")
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

    @GetMapping(value = "/report", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    public byte[] createReport() {
        return reportService.exportAsPdf();
    }
}
