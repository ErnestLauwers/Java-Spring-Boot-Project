package be.ucll.ip.minor.groep5610.team.web;

import be.ucll.ip.minor.groep5610.team.domain.Team;
import be.ucll.ip.minor.groep5610.team.domain.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
            createSampleData();
            teams = teamService.getTeams();
        }
        return ResponseEntity.ok().body(teams);
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

}
