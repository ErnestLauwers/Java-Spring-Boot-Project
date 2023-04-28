package be.ucll.ip.minor.groep5610.regatta.domain;

import be.ucll.ip.minor.groep5610.regatta.web.RegattaDto;
import be.ucll.ip.minor.groep5610.regatta.web.RegattaSearchDto;
import be.ucll.ip.minor.groep5610.team.domain.Team;
import be.ucll.ip.minor.groep5610.team.domain.TeamService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RegattaService {

    @Autowired
    private RegattaRepository regattaRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MessageSource messageSource;

    public Page<Regatta> getRegattaPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return regattaRepository.findAll(pageable);
    }

    public Regatta createRegatta(RegattaDto dto) {
        Regatta existingRegatta = regattaRepository.findByNameAndDateAndWedstrijdNaam(dto.getName(), dto.getDate(), dto.getWedstrijdNaam());
        if (existingRegatta != null) {
            String message = messageSource.getMessage("combination.club.datum.wedstrijd.is.not.unique", null, LocaleContextHolder.getLocale());
            throw new ServiceException(message);
        }

        Regatta regatta = new Regatta();
        regatta.setWedstrijdNaam(dto.getWedstrijdNaam());
        regatta.setName(dto.getName());
        regatta.setDate(dto.getDate());
        regatta.setMaxTeams(dto.getMaxTeams());
        regatta.setCategorie(dto.getCategorie());
        return regattaRepository.save(regatta);
    }

    public Regatta getRegatta(Long id) {
        return regattaRepository.findById(id)
                .orElseThrow(() -> new ServiceException(messageSource.getMessage("no.regatta.with.this.id", null, LocaleContextHolder.getLocale())));
    }

    public void deleteRegatta(Long id) {
        regattaRepository.deleteById(id);
    }

    public void updateRegatta(RegattaDto dto, Regatta regatta){
        Regatta existingRegatta = regattaRepository.findByNameAndDateAndWedstrijdNaam(dto.getName(), dto.getDate(), dto.getWedstrijdNaam());
        if (existingRegatta != null && existingRegatta.getId() != regatta.getId()) {
            String message = messageSource.getMessage("combination.club.datum.wedstrijd.is.not.unique", null, LocaleContextHolder.getLocale());
            throw new ServiceException(message);
        }
        regatta.setWedstrijdNaam(dto.getWedstrijdNaam());
        regatta.setName(dto.getName());
        regatta.setDate(dto.getDate());
        regatta.setMaxTeams(dto.getMaxTeams());
        regatta.setCategorie(dto.getCategorie());
        regattaRepository.save(regatta);
    }

//    public Page<Regatta> sort(int page, int size, String sort, String sortDir){
//        if(sortDir.equals("asc")) {
//            return regattaRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sort)));
//        } else {
//            return regattaRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort)));
//        }
//    }
//
//    public Page<Regatta> searchBy(LocalDate dateAfter, LocalDate dateBefore, String category, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        if (category.isEmpty() && dateAfter == null && dateBefore == null) {
//            throw new ServiceException(messageSource.getMessage("regatta.search.fields.all.empty", null, LocaleContextHolder.getLocale()));
//        }
//        if ((dateAfter == null && dateBefore != null) || (dateBefore == null && dateAfter != null)) {
//            throw new ServiceException(messageSource.getMessage("regatta.search.dateAfter.or.dateBefore.empty", null, LocaleContextHolder.getLocale()));
//        }
//        if (dateAfter != null && dateAfter.isAfter(dateBefore)) {
//            throw new ServiceException(messageSource.getMessage("regatta.search.dateAfter.is.after.dateBefore", null, LocaleContextHolder.getLocale()));
//        }
//        return regattaRepository.searchBy(dateAfter, dateBefore, category, pageable);
//    }

    public Page<Regatta> searchAndSort(RegattaSearchDto searchDto, String sort, String sortDir, int page, int size) {
        if (searchDto.getDateAfter() != null && searchDto.getDateAfter().isAfter(searchDto.getDateBefore())) {
            throw new ServiceException(messageSource.getMessage("regatta.search.dateAfter.is.after.dateBefore", null, LocaleContextHolder.getLocale()));
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sort));
        return regattaRepository.searchBy(searchDto.getDateAfter(), searchDto.getDateBefore(), searchDto.getCategory(), pageable);
    }

    public Team addTeamToRegatta(Long teamId, Long regattaId) {
        Regatta regatta = getRegatta(regattaId);
        Team team = teamService.getTeam(teamId);

        if (team.isAlreadyRegisteredInRegattaOnDate(regatta.getDate())) {
            throw new ServiceException(messageSource.getMessage("team.already.registered.in.regatta.on.that.day", null, LocaleContextHolder.getLocale()));
        }

        regatta.addTeam(team);
        regattaRepository.save(regatta);
        return team;
    }

    public Set<Team> getAllTeamsInRegattaWithId(Long regattaId) {
        Regatta regatta = getRegatta(regattaId);
        return regatta.getRegisteredTeams();
    }

    public Team removeTeamFromRegatta(Long teamId, Long regattaId) {
        Regatta regatta = getRegatta(regattaId);
        Team team = teamService.getTeam(teamId);

        regatta.removeTeam(team);
        regattaRepository.save(regatta);
        return team;
    }
}
