package be.ucll.ip.minor.groep5610.regatta.web;

import be.ucll.ip.minor.groep5610.regatta.domain.Regatta;
import be.ucll.ip.minor.groep5610.regatta.domain.RegattaService;
import be.ucll.ip.minor.groep5610.team.domain.Team;
import be.ucll.ip.minor.groep5610.team.web.TeamDto;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/regatta-team")
public class RegattaTeamRestController {

    @Autowired
    private RegattaService regattaService;

    @PostMapping ("/add/team/{teamId}/to/regatta/{regattaId}")
    public TeamDto addExistingTeamToRegatta(@PathVariable("teamId") Long teamId, @PathVariable("regattaId") Long regattaId) {
        return toDto(regattaService.addTeamToRegatta(teamId, regattaId));
    }

    public static TeamDto toDto(Team team) {
        TeamDto dto = new TeamDto();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setCategory(team.getCategory());
        dto.setPassengers(team.getPassengers());
        dto.setClub(team.getClub());
        dto.setRegisteredIn(team.getRegisteredIn().stream().map(RegattaTeamRestController::toRegattaTeamDto).collect(Collectors.toList()));

        return dto;
    }

    public static RegattaTeamDto toRegattaTeamDto(Regatta regatta) {
        RegattaTeamDto dto = new RegattaTeamDto();

        dto.setName(regatta.getName());

        return dto;
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
