package be.ucll.ip.minor.groep5610.regatta.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RegattaRepository extends JpaRepository<Regatta, Long> {
    Regatta findByClubNaamAndDatumAndWedstrijdNaam(String clubNaam, LocalDate datum, String wedstrijdNaam);
}
