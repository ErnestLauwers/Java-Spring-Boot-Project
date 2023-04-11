package be.ucll.ip.minor.groep5610.team;

import be.ucll.ip.minor.groep5610.Groep5610Application;
import be.ucll.ip.minor.groep5610.team.domain.Team;
import be.ucll.ip.minor.groep5610.team.domain.TeamService;
import be.ucll.ip.minor.groep5610.team.web.TeamDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.hibernate.cfg.NotYetImplementedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Groep5610Application.class)
@AutoConfigureMockMvc
public class TeamRestControllerTest {
    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private TeamService service;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc teamRestController;

    private Team alpha, delta, invalidTeam;
    private TeamDto alphaDto, deltaDto, invalidTeamDto;

    @BeforeEach
    public void setUp() {
        alpha = TeamBuilder.aValidTeamAlpha().build();
        delta = TeamBuilder.aValidTeamDelta().build();
        invalidTeam = TeamBuilder.aTeam().withName("").withCategory("").withPassengers(0).build();

        alphaDto = TeamDtoBuilder.aValidTeamAlpha().build();
        deltaDto = TeamDtoBuilder.aValidTeamDelta().build();
        invalidTeamDto = TeamDtoBuilder.aTeam().withName("").withPassengers(0).withCategory("").build();
    }

    // OVERVIEW

    @Test
    public void givenTeams_whenGetRequestToAllTeams_thenJSONWithAllTeamsIsReturned() throws Exception {
        //given
        List<Team> teams = Arrays.asList(alpha, delta);

        //mocking
        given(service.getTeams()).willReturn(teams);

        //when
        teamRestController.perform(get("/api/team/overview")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(alphaDto.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(deltaDto.getName())));

    }

    @Test
    public void givenNoTeams_whenGetRequestToAllTeams_thenEmptyJSONIsReturned() throws Exception {
        //given

        //mocking
        given(service.getTeams()).willReturn(Collections.emptyList());

        //when
        teamRestController.perform(get("/api/team/overview")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ADD

    @Test
    public void givenNoTeams_whenPostRequestToAddAValidTeam_thenJSONWithAddedTeamIsReturned() throws Exception {
        // given
        List<Team> teams = Arrays.asList(alpha);

        // mocking
        given(service.createTeam(alphaDto)).willReturn(alpha);
        given(service.getTeams()).willReturn(teams);

        //when
        teamRestController.perform(post("/api/team/add")
                .content(mapper.writeValueAsString(alphaDto))
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Is.is(alphaDto.getName())));

    }

    @Test
    public void givenNoTeams_whenPostRequestToAddAnInvalidTeam_thenErrorsInJSONFormatAreReturned() throws Exception {
        //given

        //when
        teamRestController.perform(
                post("/api/team/add")
                .content(mapper.writeValueAsString(invalidTeamDto))
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is("team.name.invalid")))
                .andExpect(jsonPath("$.category", Is.is("team.category.invalid")))
                .andExpect(jsonPath("$.passengers", Is.is("team.number.of.passengers.invalid")));
    }

    // UPDATE

    @Test
    public void givenTeams_whenPutRequestToUpdateATeamWithValidValues_thenJSONWithUpdatedTeamIsReturned() throws Exception {
        //given
        //mocking
        given(service.updateTeam(alpha.getId(), deltaDto)).willReturn(delta);

        //when
        teamRestController.perform(
                put("/api/team/update/" + alpha.getId())
                .content(mapper.writeValueAsString(deltaDto))
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Is.is("Deta")));

    }

    @Test
    public void givenTeams_whenPutRequestToUpdateATeamWithInvalidValues_thenErrorInJSONFormatIsReturned() throws Exception {
        //given
        //mocking

        //when
        teamRestController.perform(put("/api/team/update/" + alpha.getId())
                .content(mapper.writeValueAsString(invalidTeamDto))
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name", Is.is("team.name.invalid")))
                .andExpect(jsonPath("$.category", Is.is("team.category.invalid")))
                .andExpect(jsonPath("$.passengers", Is.is("team.number.of.passengers.invalid")));

    }

    @Test
    public void givenNoTeams_whenPutRequestToUpdateATeamWithNonExistentId_thenErrorInJSONFormatIsReturned() throws Exception {
        //given
        //mocking

        //when
        teamRestController.perform(put("/api/team/update/" + 500)
                .content(mapper.writeValueAsString(invalidTeamDto))
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isNotFound());
    }

    // DELETE

    @Test
    public void givenTeams_whenDeleteRequestToDeleteATeamWithValidId_thenJSONWithDeletedTeamIsReturned() throws Exception {
        //given

        //mocking

        //when
        teamRestController.perform(delete("/api/team/delete/" + alpha.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is(alphaDto.getName())));
    }

    @Test
    public void givenNoTeams_whenDeleteRequestToDeleteATeamWithNonExistentId_thenErrorInJSONFormatIsReturned() throws Exception {
        //given

        //mocking

        //when
        teamRestController.perform(delete("/api/team/delete/" + 500)
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNotFound());
    }

    // SEARCH CATEGORY

    @Test
    public void givenTeams_whenGetRequestToSearchTeamsByCategory_thenJSONWithTeamsWithFoundTeamsIsReturned() throws Exception {
        //given
        List<Team> teams = Arrays.asList(alpha);
        String category = "abcd123";

        //mocking
        given(service.getTeamsByCategory(category)).willReturn(teams);

        //when
        teamRestController.perform(get("/api/team/search")
                .param("category", category)
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(alpha.getName())));
    }

    @Test
    public void givenNoTeams_whenGetRequestToSearchTeamsByCategory_thenEmptyJSONIsReturned() throws Exception {
        //given
        String category = "abcd123";

        //mocking
        given(service.getTeamsByCategory(category)).willReturn(Collections.emptyList());

        //when
        teamRestController.perform(get("/api/team/search")
                .param("category", category)
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // SEARCH PASSENGERS

    @Test
    public void givenTeams_whenGetRequestToSearchTeamsWithLessPassengers_thenJSONWithFoundTeamsIsReturned() throws Exception {
        //given
        List<Team> teams = Arrays.asList(delta, alpha);
        int passengers = 6;

        //mocking
        given(service.getTeamsWithLessPassengersThan(passengers)).willReturn(teams);

        //when
        teamRestController.perform(get("/api/team/search/{passengers}", passengers)
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(delta.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(alpha.getName())));
    }

    @Test
    public void givenNoTeams_whenGetRequestToSearchTeamsWithLessPassengers_thenEmptyJSONIsReturned() throws Exception {
        //given
        int passengers = 6;

        //mocking
        given(service.getTeamsWithLessPassengersThan(passengers)).willReturn(Collections.emptyList());

        //when
        teamRestController.perform(get("/api/team/search/{passengers}", passengers)
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
