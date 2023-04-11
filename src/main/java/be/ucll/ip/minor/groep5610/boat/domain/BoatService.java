package be.ucll.ip.minor.groep5610.boat.domain;

import be.ucll.ip.minor.groep5610.boat.web.BoatDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
        return boatRepository.findById(id).orElseThrow(() -> new RuntimeException(("Boat with id " + id + " not found")));
    }

    public Boat getBoatByInsurance(String insuranceNumber) {
        Boat foundBoat = boatRepository.findBoatByInsurance(insuranceNumber);
        if (foundBoat == null) {
            throw new RuntimeException("There is no Boat with " + insuranceNumber + " as insurance number.");
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
            throw new RuntimeException("There is no Boat with " + height + " as height and " + width + " as width.");
        } else {
            return boatRepository.findBoatByHeightWidth(height, width);
        }
    }

    public Boat createBoat(BoatDto dto) {
        Boat existingBoat = boatRepository.findByNameAndEmail(dto.getName(), dto.getEmail());
        if (existingBoat != null) {
            String message = messageSource.getMessage("boat.combination.not.unique", null, null);
            throw new IllegalArgumentException(message);
        }
        if (boatRepository.findBoatByInsurance(dto.getInsuranceNumber()) != null) {
            String message = messageSource.getMessage("boat.insurance.number.unique", null, null);
            throw new IllegalArgumentException(message);
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

    public void updateBoat(Long id, BoatDto updatedBoat) {
        Boat existingBoat = boatRepository.findByNameAndEmail(updatedBoat.getName(), updatedBoat.getEmail());
        if (existingBoat != null) {
            if (existingBoat.getId() == id) {
                existingBoat = null;
            } else {
                String message = messageSource.getMessage("boat.combination.not.unique", null, null);
                throw new IllegalArgumentException(message);
            }
        }
        existingBoat = boatRepository.findBoatByInsurance(updatedBoat.getInsuranceNumber());
        if (existingBoat != null) {
            if (existingBoat.getId() == id) {
                existingBoat = null;
            } else {
                String message = messageSource.getMessage("boat.insurance.number.unique", null, null);
                throw new IllegalArgumentException(message);
            }
        }
        Boat oldBoat = getBoat(id);
        if (oldBoat != null) {
            oldBoat.setName(updatedBoat.getName());
            oldBoat.setEmail(updatedBoat.getEmail());
            oldBoat.setLength(updatedBoat.getLength());
            oldBoat.setWidth(updatedBoat.getWidth());
            oldBoat.setHeight(updatedBoat.getHeight());
            oldBoat.setInsuranceNumber(updatedBoat.getInsuranceNumber());
            boatRepository.save(oldBoat);
        }
    }
}
