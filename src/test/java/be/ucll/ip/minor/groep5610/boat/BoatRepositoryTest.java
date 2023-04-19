package be.ucll.ip.minor.groep5610.boat;

import be.ucll.ip.minor.groep5610.boat.domain.Boat;
import be.ucll.ip.minor.groep5610.boat.domain.BoatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BoatRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BoatRepository boatRepository;

    @Test
    public void givenThereAreBoats_whenSearchingABoatByInsuranceAndItMatches_thenThatBoatIsReturned() {
        Boat boat1 = new Boat();
        boat1.setName("Boat1");
        boat1.setEmail("boat1@example.com");
        boat1.setLength(10);
        boat1.setWidth(5);
        boat1.setHeight(3);
        boat1.setInsuranceNumber("INS000001");
        entityManager.persist(boat1);

        Boat boat = boatRepository.findBoatByInsurance("INS00000%");
        assertNotNull(boat);
        assertEquals("Boat1", boat.getName());
        assertEquals("boat1@example.com", boat.getEmail());
        assertEquals(10, boat.getLength());
        assertEquals(5, boat.getWidth());
        assertEquals(3, boat.getHeight());
        assertEquals("INS000001", boat.getInsuranceNumber());
    }

    @Test
    public void givenThereAreBoats_whenSearchingABoatByInsuranceAndItDoesntMatch_thenNULLIsReturned() {
        Boat boat1 = new Boat();
        boat1.setName("Boat1");
        boat1.setEmail("boat1@example.com");
        boat1.setLength(10);
        boat1.setWidth(5);
        boat1.setHeight(3);
        boat1.setInsuranceNumber("INS000001");
        entityManager.persist(boat1);

        Boat boat = boatRepository.findBoatByInsurance("INS000002");
        assertNull(boat);
    }

    @Test
    public void testFindBoatByHeightWidth() {
        Boat boat1 = new Boat();
        boat1.setName("Boat1");
        boat1.setEmail("boat1@example.com");
        boat1.setLength(10);
        boat1.setWidth(5);
        boat1.setHeight(5);
        boat1.setInsuranceNumber("INS000001");
        entityManager.persist(boat1);

        Boat boat2 = new Boat();
        boat2.setName("Boat2");
        boat2.setEmail("boat2@example.com");
        boat2.setLength(8);
        boat2.setWidth(5);
        boat2.setHeight(5);
        boat2.setInsuranceNumber("INS000002");
        entityManager.persist(boat2);

        List<Boat> boats = boatRepository.findBoatByHeightWidth(5, 5);

        assertEquals(2, boats.size());
        assertTrue(boats.contains(boat1));
        assertTrue(boats.contains(boat2));
    }

    @Test
    public void testFindBoatByHeightWidthNoResult() {
        Boat boat1 = new Boat();
        boat1.setName("Boat1");
        boat1.setEmail("boat1@example.com");
        boat1.setLength(10);
        boat1.setWidth(9);
        boat1.setHeight(5);
        boat1.setInsuranceNumber("INS000001");
        entityManager.persist(boat1);

        Boat boat2 = new Boat();
        boat2.setName("Boat2");
        boat2.setEmail("boat2@example.com");
        boat2.setLength(8);
        boat2.setWidth(7);
        boat2.setHeight(3);
        boat2.setInsuranceNumber("INS000002");
        entityManager.persist(boat2);

        List<Boat> boats = boatRepository.findBoatByHeightWidth(5, 5);
        assertTrue(boats.isEmpty());
    }

}