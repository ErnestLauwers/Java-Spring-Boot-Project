package be.ucll.ip.minor.groep5610.regatta.domain;

import be.ucll.ip.minor.groep5610.regatta.web.RegattaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

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
        /*Regatta existingRegatta = regattaRepository.findByClubNaamAndDatumAndWedstrijdNaam(dto.getClubNaam(), dto.getDatum(), dto.getWedstrijdNaam());
        if (existingRegatta != null) {
            String message = messageSource.getMessage("combination.club.datum.wedstrijd.is.not.unique", null, null);
            throw new IllegalArgumentException(message);
        }*/
        regatta.setWedstrijdNaam(dto.getWedstrijdNaam());
        regatta.setClubNaam(dto.getClubNaam());
        regatta.setDatum(dto.getDatum());
        regatta.setMaxTeams(dto.getMaxTeams());
        regatta.setCategorie(dto.getCategorie());
        regattaRepository.save(regatta);
    }
}
