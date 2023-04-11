package be.ucll.ip.minor.groep5610.boat.web;

import be.ucll.ip.minor.groep5610.boat.domain.Boat;
import be.ucll.ip.minor.groep5610.boat.domain.BoatRepository;
import be.ucll.ip.minor.groep5610.boat.domain.BoatService;
import be.ucll.ip.minor.groep5610.regatta.web.RegattaController;
import be.ucll.ip.minor.groep5610.storage.domain.Storage;
import be.ucll.ip.minor.groep5610.storage.web.StorageDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boat")
public class BoatController {

    private static Logger LOGGER = LoggerFactory.getLogger(BoatController.class);

    private final BoatService boatService;

    @Autowired
    public BoatController(BoatService boatService) {
        this.boatService = boatService;
    }

    @GetMapping("/overview")
    public ResponseEntity<List<Boat>> overview() {
        List<Boat> boats = boatService.getAllBoats();
        if (boats.isEmpty()) {
            createSampleData();
            boats = boatService.getAllBoats();
        }
        return ResponseEntity.ok().body(boats);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Long id) {
        try {
            Boat deletedBoat = boatService.getBoat(id);
            boatService.deleteBoatById(id);
            return ResponseEntity.ok().body(deletedBoat);
        } catch (RuntimeException exc) {
            return ResponseEntity.badRequest().body(exc.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody BoatDto boatDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            try {
                Boat newBoat = boatService.createBoat(boatDto);
                return ResponseEntity.ok().body(newBoat);
            } catch (IllegalArgumentException exc) {
                return ResponseEntity.badRequest().body(exc.getMessage());
            }
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam("id") Long id, @Valid @RequestBody BoatDto boatDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            try {
                boatService.updateBoat(id, boatDto);
                Boat updatedBoat = boatService.getBoat(id);
                return ResponseEntity.ok().body(updatedBoat);
            } catch (RuntimeException exc) {
                return ResponseEntity.badRequest().body(exc.getMessage());
            }
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByInsurance(@RequestParam("insurance") String insurance) {
        try {
            return ResponseEntity.ok().body(boatService.getBoatByInsurance(insurance));
        } catch (RuntimeException exc) {
            return ResponseEntity.badRequest().body(exc.getMessage());
        }
    }

    @GetMapping("/search/{height}/{width}")
    public ResponseEntity<?> searchByHeightWidth(@PathVariable("height") int height, @PathVariable("width") int width) {
        try {
            return ResponseEntity.ok().body(boatService.getBoatByHeightWidth(height, width));
        } catch (RuntimeException exc) {
            return ResponseEntity.badRequest().body(exc.getMessage());
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
