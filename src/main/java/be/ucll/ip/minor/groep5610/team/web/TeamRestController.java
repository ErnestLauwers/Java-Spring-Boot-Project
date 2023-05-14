package be.ucll.ip.minor.groep5610.team.web;

import be.ucll.ip.minor.groep5610.regatta.domain.Regatta;
import be.ucll.ip.minor.groep5610.regatta.web.RegattaTeamDto;
import be.ucll.ip.minor.groep5610.team.domain.Team;
import be.ucll.ip.minor.groep5610.team.domain.TeamService;
import jakarta.validation.Valid;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/team")
public class TeamRestController {
    private static Logger LOGGER = LoggerFactory.getLogger(TeamRestController.class);

    private final TeamService teamService;

    @Autowired
    public TeamRestController(TeamService teamService){
        this.teamService = teamService;
    }

    @GetMapping("/overview")
    public ResponseEntity<List<TeamDto>> overview(){
        List<Team> teams = teamService.getTeams();

        if(teams.isEmpty()){
            createSampleData();
            teams = teamService.getTeams();
        }
        return ResponseEntity.ok().body(teams.stream().map(TeamRestController::toDto).collect(Collectors.toList()));
    }

    @PostMapping("/add")
    public TeamDto add(@Valid @RequestBody TeamDto teamDto) {
        return toDto(teamService.createTeam(teamDto));
    }

    @PutMapping("/update/{id}")
    public TeamDto update(@PathVariable("id") Long id, @Valid @RequestBody TeamDto teamDto) {
        return toDto(teamService.updateTeam(id, teamDto));
    }

    @DeleteMapping("/delete/{id}")
    public TeamDto delete(@PathVariable("id") Long id){
        TeamDto deletedTeam = toDto(teamService.getTeam(id));
        teamService.deleteTeamById(id);
        return deletedTeam;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByCategory(@RequestParam("category") String category){
        try {
            List<Team> teams = teamService.getTeamsByCategory(category);
            return ResponseEntity.ok().body(teams.stream().map(TeamRestController::toDto).collect(Collectors.toList()));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search/{number}")
    public ResponseEntity<?> searchByPassengers(@PathVariable("number") int passengers) {
        try {
            List<Team> teams = teamService.getTeamsWithLessPassengersThan(passengers);
            return ResponseEntity.ok().body(teams.stream().map(TeamRestController::toDto).collect(Collectors.toList()));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public static TeamDto toDto(Team team) {
        TeamDto dto = new TeamDto();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setCategory(team.getCategory());
        dto.setPassengers(team.getPassengers());
        dto.setClub(team.getClub());
        dto.setRegattaNames(team.getRegisteredIn().stream().map(TeamRestController::toRegattaTeamDto).collect(Collectors.toList()));

        return dto;
    }

    public static RegattaTeamDto toRegattaTeamDto(Regatta regatta) {
        RegattaTeamDto dto = new RegattaTeamDto();

        dto.setName(regatta.getName());

        return dto;
    }

    private void createSampleData() {
        TeamDto team1 = new TeamDto();
        team1.setName("team1");
        team1.setCategory("catego1");
        team1.setPassengers(1);
        team1.setClub("club1");

        TeamDto team2 = new TeamDto();
        team2.setName("team2");
        team2.setCategory("catego2");
        team2.setPassengers(10);
        team2.setClub("club2");

        teamService.createTeam(team1);
        teamService.createTeam(team2);
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
