package be.ucll.ip.minor.groep5610.team;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class TeamEnd2EndTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void validateThatA400IsReturnedWhenNonValidValuesAreUsed() {
        TeamBodyValue value = new TeamBodyValue();
        value.name = "team";
        value.category = "abc";
        value.passengers = 0;
        value.club = "";

        client.post()
                .uri("/api/team/add")
                .bodyValue(value)
                .exchange()
                .expectBody()
                .json("{\"name\": \"team.name.invalid\", "
                        + "\"category\": \"team.category.invalid\", "
                        + "\"passengers\": \"team.number.of.passengers.invalid\" }");
    }

    private static class TeamBodyValue {
        public String name;
        public String category;
        public Integer passengers;
        public String club;
    }
}
