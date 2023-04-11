package be.ucll.ip.minor.groep5610.team.web;

import be.ucll.ip.minor.groep5610.team.domain.Team;
import be.ucll.ip.minor.groep5610.team.domain.TeamService;
import jakarta.validation.Valid;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

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
    public ResponseEntity<List<Team>> overview(){
        List<Team> teams = teamService.getTeams();

        if(teams.isEmpty()){
            //createSampleData();
            teams = teamService.getTeams();
        }
        return ResponseEntity.ok().body(teams);
    }

    @PostMapping("/add")
    public Iterable<Team> add(@Valid @RequestBody TeamDto teamDto){
        teamService.createTeam(teamDto);
        return teamService.getTeams();

    }

    @PutMapping("/update/{id}")
    public Iterable<Team> update(@PathVariable("id") Long id, @Valid @RequestBody TeamDto teamDto){
        /*if (teamService.getTeam(id) != null) {
            try {
                teamService.updateTeam(id, teamDto);
                Team updatedTeam = teamService.getTeam(id);
                return ResponseEntity.ok().body(updatedTeam);
            } catch (IllegalArgumentException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }*/
        teamService.updateTeam(id, teamDto);
        return teamService.getTeams();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        if(teamService.getTeam(id) != null){
            try {
                Team deletedTeam = teamService.getTeam(id);
                teamService.deleteTeamById(id);
                return ResponseEntity.ok().body(deletedTeam);
            } catch (IllegalArgumentException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByCategory(@RequestParam("category") String category){
        try {
            return ResponseEntity.ok().body(teamService.getTeamsByCategory(category));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search/{number}")
    public ResponseEntity<?> searchByPassengers(@PathVariable("number") int passengers) {
        try {
            return ResponseEntity.ok().body(teamService.getTeamsWithLessPassengersThan(passengers));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
        /*else if (ex instanceof ServiceException) {
            errors.put(((ServiceException) ex).getAction(), ex.getMessage());
        }*/
        else {
            errors.put(((ResponseStatusException) ex).getReason(), ex.getCause().getMessage());
        }
        return errors;
    }

}
