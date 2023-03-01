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

    @Query("SELECT r FROM Regatta r WHERE"
            + "((:dateAfter IS NULL OR :dateBefore IS NULL) OR r.datum BETWEEN :dateAfter AND :dateBefore)"
            + "AND (:category = '' OR LOWER(r.categorie) LIKE LOWER(CONCAT('%', :category, '%') ))")
    List<Regatta> searchBy(LocalDate dateAfter, LocalDate dateBefore, String category);
}
