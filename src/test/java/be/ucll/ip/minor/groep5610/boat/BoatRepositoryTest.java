package be.ucll.ip.minor.groep5610.boat;

import be.ucll.ip.minor.groep5610.boat.domain.Boat;
import be.ucll.ip.minor.groep5610.boat.domain.BoatRepository;
import be.ucll.ip.minor.groep5610.team.domain.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

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
        Boat boat = BoatBuilder.aBoatWithValidFields1().build();
        boat.setInsuranceNumber("ABC1234567");
        boatRepository.save(boat);

        Boat foundBoat = boatRepository.findBoatByInsurance("ABC1234567");
        assertNotNull(foundBoat);
        assertEquals(boat.getInsuranceNumber(), foundBoat.getInsuranceNumber());
    }

    @Test
    public void givenThereAreBoats_whenSearchingABoatByInsuranceAndItDoesntMatch_thenNULLIsReturned() {
        Boat boat = BoatBuilder.aBoatWithValidFields1().build();
        boat.setInsuranceNumber("ABC1234567");
        boatRepository.save(boat);

        Boat foundBoat = boatRepository.findBoatByInsurance("YUIEG45287");
        assertNull(foundBoat);
    }

    @Test
    public void testFindBoatByHeightWidth() {
        Boat validBoat1 = BoatBuilder.aBoatWithValidFields1().build();
        Boat validBoat2 = BoatBuilder.aBoatWithValidFields2().build();

        boatRepository.save(validBoat1);
        boatRepository.save(validBoat2);

        List<Boat> result = boatRepository.findBoatByHeightWidth(5, 5);
        assertEquals(2, result.size());
        assertEquals(validBoat1.getInsuranceNumber(), result.get(0).getInsuranceNumber());
        assertEquals(validBoat2.getInsuranceNumber(), result.get(1).getInsuranceNumber());
    }

    @Test
    public void testFindBoatByHeightWidthNoResult() {
        Boat validBoat1 = BoatBuilder.aBoatWithValidFields1().build();
        Boat validBoat2 = BoatBuilder.aBoatWithValidFields2().build();

        boatRepository.save(validBoat1);
        boatRepository.save(validBoat2);

        List<Boat> result = boatRepository.findBoatByHeightWidth(7, 3);
        assertTrue(result.isEmpty());
    }

}