package be.ucll.ip.minor.groep5610.team;

import be.ucll.ip.minor.groep5610.team.domain.TeamRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TeamRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeamRepository teamRepository;
}
