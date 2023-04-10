package be.ucll.ip.minor.groep5610.team.domain;

import be.ucll.ip.minor.groep5610.team.web.TeamDto;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MessageSource messageSource;

    public List<Team> getTeams() {
        return teamRepository.findAll();
    }

    public Team getTeam(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ServiceException(messageSource.getMessage("no.team.with.this.id", null, LocaleContextHolder.getLocale())));
    }

    public Team createTeam(TeamDto dto) {
        Team existingTeam = teamRepository.findByNameAndCategory(dto.getName(), dto.getCategory());
        if (existingTeam != null) {
            String message = messageSource.getMessage("team.combination.name.and.category.not.unique", null, LocaleContextHolder.getLocale());
            throw new ServiceException(message);
        }
        Team team = new Team();
        team.setName(dto.getName());
        team.setCategory(dto.getCategory());
        team.setPassengers(dto.getPassengers());
        team.setClub(dto.getClub());
        return teamRepository.save(team);
    }

    public Team updateTeam(Long id, TeamDto dto) {
        Team team = getTeam(id);
        Team existingTeam = teamRepository.findByNameAndCategory(dto.getName(), dto.getCategory());
        if (existingTeam != null && existingTeam.getId() != team.getId()) {
            String message = messageSource.getMessage("team.combination.name.and.category.not.unique", null, LocaleContextHolder.getLocale());
            throw new ServiceException(message);
        }
        team.setName(dto.getName());
        team.setCategory(dto.getCategory());
        team.setPassengers(dto.getPassengers());
        team.setClub(dto.getClub());
        return teamRepository.save(team);
    }

    public void deleteTeamById(Long id) {
        teamRepository.delete(getTeam(id));
    }

    public List<Team> getTeamsByCategory(String category) {
        return teamRepository.findByCategory(category);
    }

    public List<Team> getTeamsWithLessPassengersThan(int passengers) {
        return teamRepository.findByPassengersLessThanOrderByPassengers(passengers);
    }
}
