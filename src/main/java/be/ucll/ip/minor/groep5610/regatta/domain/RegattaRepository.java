package be.ucll.ip.minor.groep5610.regatta.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RegattaRepository extends JpaRepository<Regatta, Long> {
    Regatta findByNameAndDateAndWedstrijdNaam(String clubNaam, LocalDate datum, String wedstrijdNaam);

    @Query("SELECT r FROM Regatta r WHERE"
            + "((:dateAfter IS NULL OR :dateBefore IS NULL) OR r.date BETWEEN :dateAfter AND :dateBefore)"
            + "AND (:category = '' OR LOWER(r.categorie) LIKE LOWER(CONCAT('%', :category, '%') ))")
    Page<Regatta> searchBy(LocalDate dateAfter, LocalDate dateBefore, String category, Pageable pageable);
}
