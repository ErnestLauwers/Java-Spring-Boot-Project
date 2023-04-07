package be.ucll.ip.minor.groep5610.team;

import be.ucll.ip.minor.groep5610.team.domain.Team;
import be.ucll.ip.minor.groep5610.team.domain.TeamRepository;
import be.ucll.ip.minor.groep5610.team.domain.TeamService;
import be.ucll.ip.minor.groep5610.team.web.TeamDto;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    TeamRepository teamRepository;

    @InjectMocks
    TeamService teamService;

    @Test
    public void givenNoTeams_whenAddingAValidTeam_thenTeamIsAddedAndReturned() {
        //given
        Team teamAlpha = TeamBuilder.aValidTeamAlpha().build();
        TeamDto teamAlphaDto = TeamDtoBuilder.aValidTeamAlpha().build();

        //mocking
        when(teamRepository.findByNameAndCategory(teamAlphaDto.getName(), teamAlphaDto.getCategory())).thenReturn(null);
        when(teamRepository.save(any())).thenReturn(teamAlpha);

        //when
        Team addedTeam = teamService.createTeam(teamAlphaDto);

        //then
        assertThat(teamAlpha.getName()).isSameAs(addedTeam.getName());
        assertThat(teamAlpha.getCategory()).isSameAs(addedTeam.getCategory());
        assertThat(teamAlpha.getPassengers()).isSameAs(addedTeam.getPassengers());
        assertThat(teamAlpha.getClub()).isSameAs(addedTeam.getClub());

    }

    @Test
    public void givenTeams_whenAddingAValidTeamWithAlreadyUsedNameAndCategory_thenTeamIsNotAddedAndErrorIsReturned() {
        //given
        Team teamAlpha = TeamBuilder.aValidTeamAlpha().build();
        TeamDto teamAlphaDto = TeamDtoBuilder.aValidTeamAlpha().build();

        //mocking
        when(teamRepository.findByNameAndCategory(teamAlphaDto.getName(), teamAlphaDto.getCategory())).thenReturn(teamAlpha);

        //when
        final Throwable exception = catchThrowable(() -> teamService.createTeam(teamAlphaDto));

        //then
        assertThat(exception).isInstanceOf(ServiceException.class)
                .hasMessageContaining("team.combination.name.and.category.not.unique");
    }

    @Test
    public void givenTeams_whenRetrievingAllTeams_thenAllTeamsAreReturned() {
        //given
        Team teamAlpha = TeamBuilder.aValidTeamAlpha().build();
        Team teamDelta = TeamBuilder.aValidTeamDelta().build();

        //mocking
        when(teamRepository.findAll()).thenReturn(List.of(teamAlpha, teamDelta));

        //when
        List<Team> allTeams = teamService.getTeams();

        //then
        assertThat(allTeams).isNotEmpty();
        assertThat(allTeams).hasSize(2);
        assertThat(allTeams).contains(teamAlpha, teamDelta);
    }

    @Test
    public void givenNoTeams_whenRetrievingAllTeams_thenEmptyListIsReturned() {
        //given

        //mocking
        when(teamRepository.findAll()).thenReturn(Collections.emptyList());

        //when
        List<Team> allTeams = teamService.getTeams();

        //then
        assertThat(allTeams).isNotNull();
        assertThat(allTeams).isEmpty();
    }

    @Test
    public void givenTeams_whenRetrievingATeamById_thenTeamWithSpecifiedIdIsReturned() {
        //given
        Team teamAlpha = TeamBuilder.aValidTeamAlpha().build();

        //mocking
        when(teamRepository.findById(teamAlpha.getId())).thenReturn(Optional.of(teamAlpha));

        //when
        Team foundTeam = teamService.getTeam(teamAlpha.getId());

        //then
        assertThat(foundTeam).isNotNull();
        assertThat(foundTeam).isSameAs(teamAlpha);
    }

    @Test
    public void givenNonExistentTeam_whenRetrievingATeamById_thenErrorIsReturned() {
        //given
        Team teamAlpha = TeamBuilder.aValidTeamAlpha().build();

        //mocking
        when(teamRepository.findById(teamAlpha.getId())).thenReturn(null);

        //when
        final Throwable exception = catchThrowable(() -> teamService.getTeam(teamAlpha.getId()));

        //then
        assertThat(exception).isInstanceOf(ServiceException.class)
                .hasMessageContaining("no.team.with.this.id");
    }

    @Test
    public void givenTeams_whenUpdatingATeamWithValidIdWithNewValues_thenTeamIsUpdatedWithNewValues() {
        //given
        Team teamAlpha = TeamBuilder.aValidTeamAlpha().build();
        TeamDto dto = TeamDtoBuilder.aValidTeamDelta().build();

        //mocking
        when(teamRepository.findById(teamAlpha.getId())).thenReturn(Optional.of(teamAlpha));
        when(teamRepository.findByNameAndCategory(dto.getName(), dto.getCategory())).thenReturn(null);
        when(teamRepository.save(any())).thenReturn(teamAlpha);

        //when
        Team updatedTeam = teamService.updateTeam(teamAlpha.getId(), dto);

        //then
        assertThat(teamAlpha).isSameAs(updatedTeam);
        assertThat(updatedTeam.getName()).isEqualTo(dto.getName());
        assertThat(updatedTeam.getCategory()).isEqualTo(dto.getCategory());
        assertThat(updatedTeam.getPassengers()).isEqualTo(dto.getPassengers());
        assertThat(updatedTeam.getClub()).isEqualTo(dto.getClub());
    }

    @Test
    public void givenTeams_whenUpdatingATeamWithAlreadyUsedNameAndCategory_thenTeamIsNotUpdatedAndErrorIsReturned() {
        //given
        Team teamAlpha = TeamBuilder.aValidTeamAlpha().build();
        Team teamDelta = TeamBuilder.aValidTeamDelta().build();
        TeamDto dto = TeamDtoBuilder.aValidTeamDelta().build();

        //mocking
        when(teamRepository.findById(teamAlpha.getId())).thenReturn(Optional.of(teamAlpha));
        when(teamRepository.findByNameAndCategory(dto.getName(), dto.getCategory())).thenReturn(teamDelta);

        //when
        final Throwable exception = catchThrowable(() -> teamService.updateTeam(teamAlpha.getId(), dto));

        //then
        assertThat(exception).isInstanceOf(ServiceException.class)
                .hasMessageContaining("team.combination.name.and.category.not.unique");
    }

    @Test
    public void givenTeams_whenUpdatingATeamWithNonExistentId_thenErrorIsReturned() {
        //given
        Team nonExistentTeam = TeamBuilder.aValidTeamAlpha().build();
        TeamDto dto = TeamDtoBuilder.aValidTeamDelta().build();

        //mocking
        when(teamRepository.findById(nonExistentTeam.getId())).thenReturn(null);

        //when
        final Throwable exception = catchThrowable(() -> teamService.updateTeam(nonExistentTeam.getId(), dto));

        //then
        assertThat(exception).isInstanceOf(ServiceException.class)
                .hasMessageContaining("no.team.with.this.id");
    }

    @Test
    public void givenTeams_whenDeletingATeamWithValidId_thenTeamIsDeleted() {
        //given
        Team teamAlpha = TeamBuilder.aValidTeamAlpha().build();

        //mocking
        when(teamRepository.findById(teamAlpha.getId())).thenReturn(Optional.of(teamAlpha));
        doNothing().when(teamRepository).deleteById(teamAlpha.getId());

        //when
        teamService.deleteTeamById(teamAlpha.getId());

        //then
        verify(teamRepository, times(1)).deleteById(teamAlpha.getId());
    }

    @Test
    public void givenTeams_whenDeletingATeamWithNonExistentId_thenErrorIsReturned() {
        //given
        Team nonExistentTeam = TeamBuilder.aValidTeamAlpha().build();

        //mocking
        when(teamRepository.findById(nonExistentTeam.getId())).thenReturn(null);

        //when
        final Throwable exception = catchThrowable(() -> teamService.deleteTeamById(nonExistentTeam.getId()));

        //then
        assertThat(exception).isInstanceOf(ServiceException.class)
                .hasMessageContaining("no.team.with.this.id");
    }

    @Test
    public void givenTeams_whenSearchingTeamsByCategory_thenTeamsWithSpecifiedCategoryAreReturned() {
        //given
        Team teamAlpha = TeamBuilder.aValidTeamAlpha().build();
        String category = "abcd123";

        //mocking
        when(teamRepository.findByCategory(category)).thenReturn(List.of(teamAlpha));

        //when
        List<Team> teams = teamService.getTeamsByCategory(category);

        //then
        assertThat(teams).isNotEmpty();
        assertThat(teams).hasSize(1);
        assertThat(teams).contains(teamAlpha);
    }

    @Test
    public void givenNoTeams_whenSearchingTeamsByCategory_thenEmptyListIsReturned() {
        //given
        String category = "abcd123";

        //mocking
        when(teamRepository.findByCategory(category)).thenReturn(Collections.emptyList());

        //when
        List<Team> teams = teamService.getTeamsByCategory(category);

        //then
        assertThat(teams).isNotNull();
        assertThat(teams).isEmpty();
    }

    @Test
    public void givenTeams_whenSearchingTeamsWithLessPassengersThanSpecified_thenTeamsWithLessPassengersThanSpecifiedAreReturned() {
        //given
        Team teamAlpha = TeamBuilder.aValidTeamAlpha().build();
        Team teamDelta = TeamBuilder.aValidTeamDelta().build();
        int passengers = 6;

        //mocking
        when(teamRepository.findByPassengersLessThan(passengers)).thenReturn(List.of(teamAlpha, teamDelta));

        //when
        List<Team> teams = teamService.getTeamsWithLessPassengersThan(passengers);

        //then
        assertThat(teams).isNotEmpty();
        assertThat(teams).hasSize(2);
        assertThat(teams).contains(teamAlpha, teamDelta);
    }

    @Test
    public void givenNoTeams_whenSearchingTeamsWithLessPassengersThanSpecified_thenEmptyListIsReturned() {
        //given
        int passengers = 6;

        //mocking
        when(teamRepository.findByPassengersLessThan(passengers)).thenReturn(Collections.emptyList());

        //when
        List<Team> teams = teamService.getTeamsWithLessPassengersThan(passengers);

        //then
        assertThat(teams).isEmpty();
    }
}
