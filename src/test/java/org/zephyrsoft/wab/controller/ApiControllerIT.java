package org.zephyrsoft.wab.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.zephyrsoft.wab.model.Family;
import org.zephyrsoft.wab.model.Person;
import org.zephyrsoft.wab.repository.FamilyRepository;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FamilyRepository familyRepository;

    @Test
    void readAllFamilies() throws Exception {
        mockMvc.perform(get("/data/family"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(7)))
            .andExpect(jsonPath("$[0].lastName").value("Family 1"))
            .andExpect(jsonPath("$[0].members.length()").value(greaterThanOrEqualTo(3)))
            .andExpect(jsonPath("$[0].members[0].firstName").value("Surname 1-1"));
    }

    @Test
    void readFamily_found() throws Exception {
        mockMvc.perform(get("/data/family/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lastName").value("Family 1"))
            .andExpect(jsonPath("$.members[0].firstName").value("Surname 1-1"));
    }

    @Test
    void readFamily_notFound() throws Exception {
        mockMvc.perform(get("/data/family/999"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    void readPerson_found() throws Exception {
        mockMvc.perform(get("/data/person/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lastName").value(isNull(String.class)))
            .andExpect(jsonPath("$.firstName").value("Surname 1-1"));
    }

    @Test
    void readPerson_notFound() throws Exception {
        mockMvc.perform(get("/data/person/999"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    void createFamily_noBody() throws Exception {
        mockMvc.perform(post("/data/family"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(greaterThan(0)))
            .andExpect(jsonPath("$.lastName").value(isNull(String.class)));
    }

    @Test
    void createFamily_withBody() throws Exception {
        mockMvc.perform(post("/data/family")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":-2,\"lastName\":\"Test-Surname-Created\"}"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(greaterThan(0)))
            .andExpect(jsonPath("$.lastName").value("Test-Surname-Created"));
    }

    @Test
    void updateFamily_success() throws Exception {
        mockMvc.perform(put("/data/family/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":-2,\"lastName\":\"Test-Surname-Updated\"}"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(greaterThan(0)))
            .andExpect(jsonPath("$.lastName").value("Test-Surname-Updated"));
    }

    @Test
    void updateFamily_noId() throws Exception {
        mockMvc.perform(put("/data/family/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":-2,\"lastName\":\"Test-Surname-Wrong\"}"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    void updateFamily_negativeId() throws Exception {
        mockMvc.perform(put("/data/family/-2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":-2,\"lastName\":\"Test-Surname-Wrong\"}"))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateFamily_notFound() throws Exception {
        mockMvc.perform(put("/data/family/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":-2,\"lastName\":\"Test-Surname-Wrong\"}"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteFamily_success() throws Exception {
        mockMvc.perform(delete("/data/family/3"))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteFamily_negativeId() throws Exception {
        mockMvc.perform(delete("/data/family/-2"))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteFamily_notFound() throws Exception {
        mockMvc.perform(delete("/data/family/999"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    void createPerson_success() throws Exception {
        mockMvc.perform(post("/data/family/4"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(greaterThan(0)))
            .andExpect(jsonPath("$.firstName").value(isNull(String.class)))
            .andExpect(jsonPath("$.lastName").value(isNull(String.class)));
    }

    @Test
    void createPerson_negativeId() throws Exception {
        mockMvc.perform(post("/data/family/-2"))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void createPerson_notFound() throws Exception {
        mockMvc.perform(post("/data/family/999"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    void updatePerson_success() throws Exception {
        mockMvc.perform(put("/data/person/11")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":-2,\"firstName\":\"Test-Firstname-Updated\"}"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(greaterThan(0)))
            .andExpect(jsonPath("$.firstName").value("Test-Firstname-Updated"));
    }

    @Test
    void updatePerson_noId() throws Exception {
        mockMvc.perform(put("/data/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":-2,\"firstName\":\"Test-Firstname-Wrong\"}"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    void updatePerson_negativeId() throws Exception {
        mockMvc.perform(put("/data/person/-2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":-2,\"firstName\":\"Test-Firstname-Wrong\"}"))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void updatePerson_notFound() throws Exception {
        mockMvc.perform(put("/data/person/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":-2,\"firstName\":\"Test-Firstname-Wrong\"}"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    void deletePerson_success() throws Exception {
        mockMvc.perform(delete("/data/person/13"))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    void deletePerson_negativeId() throws Exception {
        mockMvc.perform(delete("/data/person/-2"))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void deletePerson_notFound() throws Exception {
        mockMvc.perform(delete("/data/person/999"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    void movePersonUp_success() throws Exception {
        Family familyBeforeMoveUp = familyRepository.findById(5L).orElseThrow();
        Person personBeforeMoveUp = familyBeforeMoveUp.getMembers().get(2);

        assertEquals(2, personBeforeMoveUp.getOrdering());

        mockMvc.perform(put("/data/person/" + personBeforeMoveUp.getId() + "/moveUp"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(greaterThan(0)))
            .andExpect(jsonPath("$.members[1].id").value(personBeforeMoveUp.getId()))
            .andExpect(jsonPath("$.members[1].ordering").value(personBeforeMoveUp.getOrdering() - 1))
            .andExpect(jsonPath("$.members[1].firstName").value(personBeforeMoveUp.getFirstName()));
    }

    @Test
    void movePersonUp_negativeId() throws Exception {
        mockMvc.perform(put("/data/person/-2/moveUp"))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void movePersonUp_notFound() throws Exception {
        mockMvc.perform(put("/data/person/999/moveUp"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    void movePersonUp_isAlreadyFirst() throws Exception {
        Family familyBeforeMoveUp = familyRepository.findById(5L).orElseThrow();
        Person personBeforeMoveUp = familyBeforeMoveUp.getMembers().getFirst();

        assertEquals(0, personBeforeMoveUp.getOrdering());

        mockMvc.perform(put("/data/person/" + personBeforeMoveUp.getId() + "/moveUp"))
            .andDo(print())
            .andExpect(status().isNotAcceptable());
    }

    @Test
    void movePersonDown_success() throws Exception {
        Family familyBeforeMoveDown = familyRepository.findById(5L).orElseThrow();
        Person personBeforeMoveDown = familyBeforeMoveDown.getMembers().get(2);

        assertEquals(2, personBeforeMoveDown.getOrdering());

        mockMvc.perform(put("/data/person/" + personBeforeMoveDown.getId() + "/moveDown"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(greaterThan(0)))
            .andExpect(jsonPath("$.members[3].id").value(personBeforeMoveDown.getId()))
            .andExpect(jsonPath("$.members[3].ordering").value(personBeforeMoveDown.getOrdering() + 1))
            .andExpect(jsonPath("$.members[3].firstName").value(personBeforeMoveDown.getFirstName()));
    }

    @Test
    void movePersonDown_negativeId() throws Exception {
        mockMvc.perform(put("/data/person/-2/moveDown"))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void movePersonDown_notFound() throws Exception {
        mockMvc.perform(put("/data/person/999/moveDown"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    void movePersonDown_isAlreadyLast() throws Exception {
        Family familyBeforeMoveDown = familyRepository.findById(5L).orElseThrow();
        Person personBeforeMoveDown = familyBeforeMoveDown.getMembers().getLast();

        assertEquals(familyBeforeMoveDown.getMembers().size() - 1, personBeforeMoveDown.getOrdering());

        mockMvc.perform(put("/data/person/" + personBeforeMoveDown.getId() + "/moveDown"))
            .andDo(print())
            .andExpect(status().isNotAcceptable());
    }

    @Test
    void createReport() throws Exception {
        mockMvc.perform(get("/data/report"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }
}
