package be.ucll.ip.minor.groep5610.regatta.domain;

import be.ucll.ip.minor.groep5610.regatta.web.RegattaDto;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegattaService {

    @Autowired
    private RegattaRepository regattaRepository;

    public List<Regatta> getRegattas() {
        return regattaRepository.findAll();
    }

    public Regatta createRegatta(RegattaDto dto) {
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
}
