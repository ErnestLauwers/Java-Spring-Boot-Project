package be.ucll.ip.minor.groep5610.boat.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoatRepository extends JpaRepository<Boat, Long> {
    Boat findByNameAndEmail(String name, String email);

    @Query("SELECT b from Boat b WHERE b.insuranceNumber LIKE :keyword")
    Boat findBoatByInsurance(String keyword);

    @Query("SELECT b from Boat b WHERE b.height LIKE :height AND b.width LIKE :width")
    List<Boat> findBoatByHeightWidth(int height, int width);
}
