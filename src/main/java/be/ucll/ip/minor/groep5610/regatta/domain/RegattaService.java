package be.ucll.ip.minor.groep5610.regatta.domain;

import be.ucll.ip.minor.groep5610.regatta.web.RegattaDto;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegattaService {

    @Autowired
    private RegattaRepository regattaRepository;

    @Autowired
    private MessageSource messageSource;

    public List<Regatta> getRegattas() {
        return regattaRepository.findAll();
    }

    public Regatta createRegatta(RegattaDto dto) {
        Regatta existingRegatta = regattaRepository.findByClubNaamAndDatumAndWedstrijdNaam(dto.getClubNaam(), dto.getDatum(), dto.getWedstrijdNaam());
        if (existingRegatta != null) {
            String message = messageSource.getMessage("combination.club.datum.wedstrijd.is.not.unique", null, null);
            throw new IllegalArgumentException(message);
        }

        Regatta regatta = new Regatta();
        regatta.setWedstrijdNaam(dto.getWedstrijdNaam());
        regatta.setClubNaam(dto.getClubNaam());
        regatta.setDatum(dto.getDatum());
        regatta.setMaxTeams(dto.getMaxTeams());
        regatta.setCategorie(dto.getCategorie());
        return regattaRepository.save(regatta);
    }

    public Regatta getRegatta(Long id) {
        return regattaRepository.findById(id).orElseThrow(() -> new RuntimeException("Regatta met id " + id + " niet gevonden."));
    }

    public void deleteRegatta(Long id) {
        regattaRepository.deleteById(id);
    }

    public void updateRegatta(RegattaDto dto, Regatta regatta){
        Regatta existingRegatta = regattaRepository.findByClubNaamAndDatumAndWedstrijdNaam(dto.getClubNaam(), dto.getDatum(), dto.getWedstrijdNaam());
        if (existingRegatta != null) {
            String message = messageSource.getMessage("combination.club.datum.wedstrijd.is.not.unique", null, null);
            throw new IllegalArgumentException(message);
        }
        regatta.setWedstrijdNaam(dto.getWedstrijdNaam());
        regatta.setClubNaam(dto.getClubNaam());
        regatta.setDatum(dto.getDatum());
        regatta.setMaxTeams(dto.getMaxTeams());
        regatta.setCategorie(dto.getCategorie());
        regattaRepository.save(regatta);
    }

    public List<Regatta> sortByName(){
        return regattaRepository.findByOrderByClubNaamAsc();
    }

    public List<Regatta> sortByDate(){
        return regattaRepository.findByOrderByDatumAsc();
    }

    public List<Regatta> searchBy(LocalDate dateAfter, LocalDate dateBefore, String category) {
        if (category.isEmpty() && dateAfter == null && dateBefore == null) {
            throw new ServiceException("Om regatta's te zoeken moet u minstens een start- en einddatum invullen of een categorie. U kunt ook beide invullen.");
        }
        if ((dateAfter == null && dateBefore != null) || (dateBefore == null && dateAfter != null)) {
            throw new ServiceException("Om te zoeken naar regatta's binnen een bepaalde periode, moeten zowel begin- als einddatum worden ingevuld.");
        }
        if (dateAfter != null && dateAfter.isAfter(dateBefore)) {
            throw new ServiceException("Om te zoeken naar regatta's binnen een bepaalde periode, moet de startdatum voor de einddatum liggen.");
        }
        return regattaRepository.searchBy(dateAfter, dateBefore, category);
    }
}
