package be.ucll.ip.minor.groep5610.boat.domain;

import be.ucll.ip.minor.groep5610.boat.web.BoatDto;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoatService {

    @Autowired
    private BoatRepository boatRepository;

    @Autowired
    private MessageSource messageSource;

    public List<Boat> getAllBoats() {
        return boatRepository.findAll();
    }

    public Boat getBoat(Long id) {
        return boatRepository.findById(id).orElseThrow(() -> new ServiceException(messageSource.getMessage("no.boat.with.this.id", null, LocaleContextHolder.getLocale())));
    }

    public Boat getBoatByInsurance(String insuranceNumber) {
        Boat foundBoat = boatRepository.findBoatByInsurance(insuranceNumber);
        if (foundBoat == null) {
            String message = messageSource.getMessage("no.boat.insurance.found", null, LocaleContextHolder.getLocale());
            throw new ServiceException(message);
        } else {
            return boatRepository.findBoatByInsurance(insuranceNumber);
        }
    }

    public void deleteBoatById(Long id) {
        boatRepository.deleteById(id);
    }

    public List<Boat> getBoatByHeightWidth(int height, int width) {
        List<Boat> foundBoat = boatRepository.findBoatByHeightWidth(height, width);
        if (foundBoat.isEmpty()) {
            String message = messageSource.getMessage("no.height.width.found", null, LocaleContextHolder.getLocale());
            throw new ServiceException(message);
        } else {
            return boatRepository.findBoatByHeightWidth(height, width);
        }
    }

    public Boat createBoat(BoatDto dto) {
        Boat existingBoat = boatRepository.findByNameAndEmail(dto.getName(), dto.getEmail());
        if (existingBoat != null) {
            String message = messageSource.getMessage("boat.combination.not.unique", null, null);
            throw new ServiceException(message);
        }
        if (boatRepository.findBoatByInsurance(dto.getInsuranceNumber()) != null) {
            String message = messageSource.getMessage("boat.insurance.number.unique", null, null);
            throw new ServiceException(message);
        }
        Boat boat = new Boat();
        boat.setName(dto.getName());
        boat.setEmail(dto.getEmail());
        boat.setLength(dto.getLength());
        boat.setWidth(dto.getWidth());
        boat.setHeight(dto.getHeight());
        boat.setInsuranceNumber(dto.getInsuranceNumber());
        return boatRepository.save(boat);
    }

    public Boat updateBoat(Long id, BoatDto dto) {
        Boat boat = getBoat(id);
        Boat existingBoat = boatRepository.findByNameAndEmail(dto.getName(), dto.getEmail());
        if (existingBoat != null && existingBoat.getId() != id) {
            String message = messageSource.getMessage("boat.combination.not.unique", null, LocaleContextHolder.getLocale());
            throw new ServiceException(message);
        }
        existingBoat = boatRepository.findBoatByInsurance(dto.getInsuranceNumber());
        if (existingBoat != null && existingBoat.getId() != id) {
           String message = messageSource.getMessage("boat.insurance.number.unique", null, LocaleContextHolder.getLocale());
           throw new ServiceException(message);
        }
        boat.setName(dto.getName());
        boat.setEmail(dto.getEmail());
        boat.setLength(dto.getLength());
        boat.setWidth(dto.getWidth());
        boat.setHeight(dto.getHeight());
        boat.setInsuranceNumber(dto.getInsuranceNumber());
        return boatRepository.save(boat);
    }
}
