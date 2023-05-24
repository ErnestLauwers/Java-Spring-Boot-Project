package be.ucll.ip.minor.groep5610.boat.web;

import be.ucll.ip.minor.groep5610.boat.domain.Boat;
import be.ucll.ip.minor.groep5610.boat.domain.BoatRepository;
import be.ucll.ip.minor.groep5610.boat.domain.BoatService;
import be.ucll.ip.minor.groep5610.regatta.web.RegattaController;
import be.ucll.ip.minor.groep5610.storage.domain.Storage;
import be.ucll.ip.minor.groep5610.storage.web.StorageDto;
import jakarta.validation.Valid;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<BoatDto>> overview() {
        List<Boat> boats = boatService.getAllBoats();
        if (boats.isEmpty()) {
            createSampleData();
            boats = boatService.getAllBoats();
        }
        return ResponseEntity.ok().body(boats.stream().map(BoatController::toDto).collect(Collectors.toList()));
    }

    @PostMapping("/add")
    public BoatDto add(@Valid @RequestBody BoatDto boatDto) {
        return toDto(boatService.createBoat(boatDto));
    }

    @DeleteMapping("/delete")
    public BoatDto delete(@RequestParam("id") Long id) {

        Boat deletedBoat = boatService.getBoat(id);
        boatService.deleteBoatById(id);
        return toDto(deletedBoat);

    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByInsurance(@RequestParam("insurance") String insurance) {
        try {
            return ResponseEntity.ok().body(toDto(boatService.getBoatByInsurance(insurance)));
        } catch (RuntimeException exc) {
            return ResponseEntity.badRequest().body(exc.getMessage());
        }
    }

    @GetMapping("/search/{height}/{width}")
    public ResponseEntity<?> searchByHeightWidth(@PathVariable("height") int height, @PathVariable("width") int width) {
        try {
            return ResponseEntity.ok().body(boatService.getBoatByHeightWidth(height, width).stream().map(BoatController::toDto).collect(Collectors.toList()));
        } catch (RuntimeException exc) {
            return ResponseEntity.badRequest().body(exc.getMessage());
        }
    }

    @PutMapping("/update")
    public BoatDto update(@RequestParam("id") Long id, @Valid @RequestBody BoatDto boatDto) {
        return toDto(boatService.updateBoat(id, boatDto));
    }

    public static BoatDto toDto(Boat boat) {
        BoatDto boatDto = new BoatDto();
        boatDto.setId(boat.getId());
        boatDto.setName(boat.getName());
        boatDto.setEmail(boat.getEmail());
        boatDto.setLength(boat.getLength());
        boatDto.setWidth(boat.getWidth());
        boatDto.setHeight(boat.getHeight());
        boatDto.setInsuranceNumber(boat.getInsuranceNumber());
        if (boat.getStorage() == null) {
            boatDto.setStorageName("unknown");
        } else {
            boatDto.setStorageName(boat.getStorage().getName());
        }
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ServiceException.class, ResponseStatusException.class})
    public Map<String, String> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException)ex).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        }
        else if (ex instanceof ServiceException) {
            errors.put("error", ex.getMessage());
        }
        else {
            errors.put(((ResponseStatusException) ex).getReason(), ex.getCause().getMessage());
        }
        return errors;
    }
}
