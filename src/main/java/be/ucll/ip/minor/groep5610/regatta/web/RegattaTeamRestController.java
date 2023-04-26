package be.ucll.ip.minor.groep5610.regatta.web;

import be.ucll.ip.minor.groep5610.regatta.domain.Regatta;
import be.ucll.ip.minor.groep5610.regatta.domain.RegattaService;
import be.ucll.ip.minor.groep5610.team.domain.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/regatta-team")
public class RegattaTeamRestController {

    @Autowired
    private RegattaService regattaService;

    @PostMapping ("/add/team/{teamId}/to/regatta/{regattaId}")
    public RegattaDto addExistingTeamToRegatta(@PathVariable("teamId") Long teamId, @PathVariable("regattaId") Long regattaId) {
        return toDto(regattaService.addTeamToRegatta(teamId, regattaId));
    }

    public static RegattaDto toDto(Regatta regatta) {
        RegattaDto regattaDto = new RegattaDto();
        regattaDto.setId(regatta.getId());
        regattaDto.setRegisteredTeams(regatta.getRegisteredTeams().stream().map(RegattaTeamRestController::toRegattaTeamDto).collect(Collectors.toList()));
        regattaDto.setWedstrijdNaam(regatta.getWedstrijdNaam());
        regattaDto.setName(regatta.getName());
        regattaDto.setDate(regatta.getDate());
        regattaDto.setMaxTeams(regatta.getMaxTeams());
        regattaDto.setCategorie(regatta.getCategorie());
        return regattaDto;
    }

    public static RegattaTeamDto toRegattaTeamDto(Team team) {
        RegattaTeamDto dto = new RegattaTeamDto();

        dto.setName(team.getName());

        return dto;
    }
}
