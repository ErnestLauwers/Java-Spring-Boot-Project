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
        return boatRepository.findById(id).orElseThrow(() -> new RuntimeException("Boat with id " + id + " not found"));
    }

    public Boat getBoatByInsurance(String insuranceNumber) {
        return boatRepository.findBoatByInsurance(insuranceNumber);
    }

    public void deleteBoatById(Long id) {
        boatRepository.deleteById(id);
    }

    public Boat getBoatByHeightWidth(int height, int width) {
        return boatRepository.findBoatByHeightWidth(height, width);
    }

    public Boat createBoat(BoatDto dto) {
        Boat boat = new Boat();
        boat.setName(dto.getName());
        boat.setEmail(dto.getEmail());
        boat.setLength(dto.getLength());
        boat.setWidth(dto.getWidth());
        boat.setHeight(dto.getHeight());
        boat.setInsuranceNumber(dto.getInsuranceNumber());
        return boatRepository.save(boat);
    }

    public void updateBoat(Long id, Boat updatedBoat) {
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
