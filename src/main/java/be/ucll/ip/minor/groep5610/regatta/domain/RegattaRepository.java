package be.ucll.ip.minor.groep5610.regatta.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegattaRepository extends JpaRepository<Regatta, Long> {
    Regatta findByClubNaamAndDatumAndWedstrijdNaam(String clubNaam, LocalDate datum, String wedstrijdNaam);

    List<Regatta> findByOrderByClubNaamAsc();

    List<Regatta> findByOrderByDatumAsc();

    List<Regatta> findByCategorie(String category);

    @Query("SELECT r FROM Regatta r WHERE r.datum BETWEEN :dateAfter AND :dateBefore")
    List<Regatta> findWithinRange(LocalDate dateAfter, LocalDate dateBefore);
}
