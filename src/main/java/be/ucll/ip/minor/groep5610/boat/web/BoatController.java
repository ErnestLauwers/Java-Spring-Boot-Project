package be.ucll.ip.minor.groep5610.boat.web;

import be.ucll.ip.minor.groep5610.boat.domain.Boat;
import be.ucll.ip.minor.groep5610.boat.domain.BoatRepository;
import be.ucll.ip.minor.groep5610.boat.domain.BoatService;
import be.ucll.ip.minor.groep5610.storage.domain.Storage;
import be.ucll.ip.minor.groep5610.storage.web.StorageDto;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class BoatController {

    private final BoatService boatService;

    @Autowired
    public BoatController(BoatService boatService) {
        this.boatService = boatService;
    }

    @GetMapping("/api/boat/overview")
    public ResponseEntity<List<Boat>> overview() {
        List<Boat> boats = boatService.getAllBoats();
        if (boats.isEmpty()) {
            createSampleData();
            boats = boatService.getAllBoats();
        }
        return ResponseEntity.ok().body(boats);
    }

    @DeleteMapping("/api/boat/delete")
    public ResponseEntity<Boat> delete(@RequestParam("id") Long id) {
        if (boatService.getBoat(id) != null) {
            Boat deletedBoat = boatService.getBoat(id);
            boatService.deleteBoatById(id);
            return ResponseEntity.ok().body(deletedBoat);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/boat/add")
    public ResponseEntity<?> add(@RequestBody Boat boat) {
        try {
            Boat newBoat = boatService.createBoat(toDto(boat));
            return ResponseEntity.ok().body(newBoat);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/api/boat/update")
    public ResponseEntity<?> update(@RequestParam("id") Long id, @RequestBody Boat boat) {
        if (boatService.getBoat(id) != null) {
            try {
                boatService.updateBoat(id, boat);
                Boat updatedBoat = boatService.getBoat(id);
                return ResponseEntity.ok().body(updatedBoat);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("api/boat/search")
    public ResponseEntity<Boat> searchByInsurance(@RequestParam("insurance") String insurance) {
        if (boatService.getBoatByInsurance(insurance) != null) {
            return ResponseEntity.ok().body(boatService.getBoatByInsurance(insurance));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("api/boat/search/{height}/{width}")
    public ResponseEntity<Boat> searchByHeightWidth(@PathVariable("height") int height, @PathVariable("width") int width) {
        if (boatService.getBoatByHeightWidth(height, width) != null) {
            return ResponseEntity.ok().body(boatService.getBoatByHeightWidth(height, width));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public BoatDto toDto(Boat boat) {
        BoatDto boatDto = new BoatDto();
        boatDto.setId(boat.getId());
        boatDto.setName(boat.getName());
        boatDto.setEmail(boat.getEmail());
        boatDto.setLength(boat.getLength());
        boatDto.setWidth(boat.getWidth());
        boatDto.setHeight(boat.getHeight());
        boatDto.setInsuranceNumber(boat.getInsuranceNumber());
        return boatDto;
    }

    private void createSampleData() {
        BoatDto boat1 = new BoatDto();
        boat1.setName("Rosa2016");
        boat1.setEmail("mark.johnson@gmail.com");
        boat1.setLength(21);
        boat1.setWidth(4);
        boat1.setHeight(5);
        boat1.setInsuranceNumber("1234567891");

        BoatDto boat2 = new BoatDto();
        boat2.setName("Monster3");
        boat2.setEmail("emily.parks@gmail.com");
        boat2.setLength(13);
        boat2.setWidth(3);
        boat2.setHeight(7);
        boat2.setInsuranceNumber("9876543210");

        boatService.createBoat(boat1);
        boatService.createBoat(boat2);
    }
}
