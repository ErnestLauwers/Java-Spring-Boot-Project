package be.ucll.ip.minor.groep5610.regatta.web;

import be.ucll.ip.minor.groep5610.regatta.domain.Regatta;
import be.ucll.ip.minor.groep5610.regatta.domain.RegattaService;
import be.ucll.ip.minor.groep5610.team.domain.Team;
import be.ucll.ip.minor.groep5610.team.web.TeamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
