package be.ucll.ip.minor.groep5610.storage.web;

import be.ucll.ip.minor.groep5610.boat.domain.Boat;
import be.ucll.ip.minor.groep5610.boat.web.BoatController;
import be.ucll.ip.minor.groep5610.boat.web.BoatDto;
import be.ucll.ip.minor.groep5610.storage.domain.StorageService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/storage-boat")
public class StorageBoatController {

    private final StorageService storageService;

    @Autowired
    public StorageBoatController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping ("/add/boat/{boatId}/to/storage/{storageId}")
    public BoatDto addBoatToStorage(@PathVariable("boatId") Long boatId, @PathVariable("storageId") Long storageId) {
        return toDto(storageService.addBoatToStorage(boatId, storageId));
    }

    @GetMapping ("/boats")
    public List<BoatDto> getAllBoatsInStorage(@RequestParam("storageId") Long storageId) {
        return storageService.getAllBoatsInStorage(storageId).stream().map(StorageBoatController::toDto).collect(Collectors.toList());
    }

    @PostMapping ("/remove/boat/{boatId}/from/storage/{storageId}")
    public BoatDto removeBoatFromStorage(@PathVariable("boatId") Long boatId, @PathVariable("storageId") Long storageId) {
        return toDto(storageService.removeBoatFromStorage(boatId, storageId));
    }

    public static BoatDto toDto(Boat boat) {
        BoatDto boatDto = new BoatDto();
        boatDto.setId(boat.getId());
        boatDto.setName(boat.getName());
        boatDto.setEmail(boat.getEmail());
        boatDto.setLength(boat.getLength());
        boatDto.setWidth(boat.getWidth());
        boatDto.setHeight(boat.getHeight());
        boatDto.setInsuranceNumber((boat.getInsuranceNumber()));
        if (boat.getStorage() == null) {
            boatDto.setStorageName("unknown");
        } else {
            boatDto.setStorageName(boat.getStorage().getName());
        }
        return boatDto;
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
