package be.ucll.ip.minor.groep5610.team;

import be.ucll.ip.minor.groep5610.team.domain.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TeamEnd2EndTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void validateThatA200IsReturnedWithAnOverviewOfTeams() {
        addValidTeam("Alpha", "AbCd123", 5, "ClubA");
        addValidTeam("Delta", "qztg581", 4, "ClubB");

        client.get()
                .uri("/api/team/overview")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("[{\"name\": \"Alpha\", " + "\"category\" :  \"AbCd123\", " + "\"passengers\": 5, " + "\"club\": \"ClubA\"}" +
                        ",{\"name\": \"Delta\", " + "\"category\" :  \"qztg581\", " + "\"passengers\": 4, " + "\"club\": \"ClubB\"}]");
    }

    @Test
    public void validateThatA200IsReturnedWithAddedTeamWhenValidValuesAreUsedWhenAddingATeam() {
        addValidTeam("Alpha", "AbCd123", 5, "ClubA");
    }

    @Test
    public void validateThatA400IsReturnedWithErrorsWhenNonValidValuesAreUsedWhenAddingATeam() {
        TeamBodyValue value = new TeamBodyValue();
        value.name = "team";
        value.category = "abc";
        value.passengers = 0;
        value.club = "";

        client.post()
                .uri("/api/team/add")
                .bodyValue(value)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .json("{\"name\": \"Team name must be at least 5 characters long\", "
                        + "\"category\": \"Category must be a sequence of 7 characters containing only letters (a-z, A-Z) and / or digits (0-9)\", "
                        + "\"passengers\": \"Number of passengers must be between 1 and 12\" }");
    }

    @Test
    public void validateThatA400IsReturnedWithErrorWhenAddingATeamThatAlreadyExists() {
        // add team
        addValidTeam("Alpha", "AbCd123", 5, "ClubA");

        //add team again
        TeamBodyValue value = new TeamBodyValue();
        value.name = "Alpha";
        value.category = "AbCd123";
        value.passengers = 5;
        value.club = "ClubA";

        client.post()
                .uri("/api/team/add")
                .bodyValue(value)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .json("{\"error\": \"There is already a team with that name and category\"}");
    }

    @Test
    public void validateThatA200IsReturnedWithUpdatedTeamWhenValidValuesAreUsedWhenUpdatingATeam() {
        // add team
        Team addedTeam = addValidTeam("Alpha", "AbCd123", 5, "ClubA");

        // update team with valid values
        TeamBodyValue value = new TeamBodyValue();
        value.name = "team1";
        value.category = "abcdefg";
        value.passengers = 7;
        value.club = "clubA";

        client.put()
                .uri("/api/team/update/" + addedTeam.getId())
                .bodyValue(value)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("{\"name\": \"team1\", " +
                        "\"category\" :  \"abcdefg\", " +
                        "\"passengers\": 7, " +
                        "\"club\": \"clubA\"}");
    }

    @Test
    public void validateThatA400IsReturnedWhenNonValidValuesAreUsedWhenUpdatingATeam() {
        // add team
        Team addedTeam = addValidTeam("Alpha", "AbCd123", 5, "ClubA");

        // update team with invalid values
        TeamBodyValue invalidValue = new TeamBodyValue();
        invalidValue.name = "team";
        invalidValue.category = "abc";
        invalidValue.passengers = 0;
        invalidValue.club = "";

        client.put()
                .uri("/api/team/update/" + addedTeam.getId())
                .bodyValue(invalidValue)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .json("{\"name\": \"Team name must be at least 5 characters long\", "
                        + "\"category\": \"Category must be a sequence of 7 characters containing only letters (a-z, A-Z) and / or digits (0-9)\", "
                        + "\"passengers\": \"Number of passengers must be between 1 and 12\" }");
    }

    @Test
    public void validateThatA400IsReturnedWhenUsingAnInvalidIdWhenUpdatingATeam() {
        TeamBodyValue value = new TeamBodyValue();
        value.name = "Alpha";
        value.category = "AbCd123";
        value.passengers = 5;
        value.club = "ClubA";

        client.put()
                .uri("/api/team/update/500")
                .bodyValue(value)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .json("{\"error\": \"Team with this id not found\"}");
    }

    @Test
    public void validateThatA200IsReturnedWithTheDeletedTeamWhenDeletingATeam() {
        // add team
        Team addedTeam = addValidTeam("Alpha", "AbCd123", 5, "ClubA");

        // delete the added team
        client.delete()
                .uri("/api/team/delete/{id}", addedTeam.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("{\"name\": \"Alpha\", " +
                        "\"category\" :  \"AbCd123\", " +
                        "\"passengers\": 5, " +
                        "\"club\": \"ClubA\"}");
    }

    @Test
    public void validateThatA400IsReturnedWhenAnInvalidIdIsUsedWhenDeletingTeam() {
        client.delete()
                .uri("/api/team/delete/500")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .json("{\"error\": \"Team with this id not found\"}");
    }

    @Test
    public void validateThatA200IsReturnedWithAListOfFoundTeamsWhenSearchingByCategory() {
        // add teams
        addValidTeam("Alpha", "AbCd123", 5, "ClubA");

        // search by category
        client.get()
                .uri("/api/team/search?category=abcd123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("[{\"name\": \"Alpha\", " +
                        "\"category\" :  \"AbCd123\", " +
                        "\"passengers\": 5, " +
                        "\"club\": \"ClubA\"}]");
    }

    @Test
    public void validateThatA200IsReturnedWithAnOrderedListOfFoundTeamsWhenSearchingTeamsWithLessPassengers() {
        // add teams
        addValidTeam("Alpha", "AbCd123", 5, "ClubA");
        addValidTeam("Delta", "qztg581", 4, "ClubB");

        // search with fewer passengers than 5
        client.get()
                .uri("/api/team/search/7")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("[{\"name\": \"Delta\", " + "\"category\" :  \"qztg581\", " + "\"passengers\": 4, " + "\"club\": \"ClubB\"} , " +
                        "{\"name\": \"Alpha\", " + "\"category\" :  \"AbCd123\", " + "\"passengers\": 5, " + "\"club\": \"ClubA\"}]");
    }

    public Team addValidTeam(String name, String category, Integer passengers, String club) {
        TeamBodyValue value = new TeamBodyValue();
        value.name = name;
        value.category = category;
        value.passengers = passengers;
        value.club = club;

        return client.post()
                .uri("/api/team/add")
                .bodyValue(value)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Team.class)
                .returnResult()
                .getResponseBody();
    }

    private static class TeamBodyValue {
        public String name;
        public String category;
        public Integer passengers;
        public String club;
    }
}
