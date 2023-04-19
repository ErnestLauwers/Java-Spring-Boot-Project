package be.ucll.ip.minor.groep5610.boat;

import be.ucll.ip.minor.groep5610.boat.domain.Boat;
import be.ucll.ip.minor.groep5610.boat.domain.BoatRepository;
import be.ucll.ip.minor.groep5610.boat.domain.BoatService;
import be.ucll.ip.minor.groep5610.boat.web.BoatDto;
import be.ucll.ip.minor.groep5610.team.TeamBuilder;
import be.ucll.ip.minor.groep5610.team.TeamDtoBuilder;
import be.ucll.ip.minor.groep5610.team.domain.Team;
import be.ucll.ip.minor.groep5610.team.domain.TeamRepository;
import be.ucll.ip.minor.groep5610.team.domain.TeamService;
import be.ucll.ip.minor.groep5610.team.web.TeamDto;
import net.bytebuddy.implementation.bytecode.Throw;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoatServiceTest {

    @Mock
    BoatRepository boatRepository;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    BoatService boatService;

    //OVERVIEW

    @Test
    public void givenThereAreNoBoats_whenRetrievingAllBoats_thenAnEmptyListIsReturned() {
        when(boatRepository.findAll()).thenReturn(Collections.emptyList());
        List<Boat> boats = boatService.getAllBoats();
        assertThat(boats).isNotNull();
        assertThat(boats).isEmpty();
    }

    @Test
    public void givenThereAreBoats_whenRetrievingAllBoats_thenAllBoatsAreReturned() {
        Boat validBoat1 = BoatBuilder.aBoatWithValidFields1().build();
        Boat validBoat2 = BoatBuilder.aBoatWithValidFields2().build();

        when(boatRepository.findAll()).thenReturn(List.of(validBoat1, validBoat2));

        List<Boat> boats = boatService.getAllBoats();

        assertThat(boats).isNotNull();
        assertThat(boats).hasSize(2);
        assertThat(boats).contains(validBoat1, validBoat2);
    }

    //ADD

    @Test
    public void givenThereAreNoBoats_whenAddingAValidBoat_thenBoatIsAddedAndReturned() {
        Boat validBoat1 = BoatBuilder.aBoatWithValidFields1().build();
        BoatDto validBoatDto1 = BoatDtoBuilder.aBoatWithValidFields1().build();

        when(boatRepository.findBoatByInsurance(validBoatDto1.getInsuranceNumber())).thenReturn(null);
        when(boatRepository.save(any())).thenReturn(validBoat1);

        Boat boat = boatService.createBoat(validBoatDto1);

        assertThat(validBoat1.getName()).isSameAs(boat.getName());
        assertThat(validBoat1.getEmail()).isSameAs(boat.getEmail());
        assertThat(validBoat1.getInsuranceNumber()).isSameAs(boat.getInsuranceNumber());
        assertThat(validBoat1.getLength()).isSameAs(boat.getLength());
        assertThat(validBoat1.getWidth()).isSameAs(boat.getWidth());
        assertThat(validBoat1.getHeight()).isSameAs(boat.getHeight());
    }

    @Test
    public void givenThereAreBoats_whenAddingAValidBoatWithAlreadyUsedInsuranceNumber_thenBoatIsNotAddedAndAnErrorReturned() {
        Boat validBoat1 = BoatBuilder.aBoatWithValidFields1().build();
        BoatDto validBoatDto1 = BoatDtoBuilder.aBoatWithValidFields1().build();

        when(boatRepository.findBoatByInsurance(validBoatDto1.getInsuranceNumber())).thenReturn(validBoat1);
        when(messageSource.getMessage(eq("boat.insurance.number.unique"), any(), any())).thenReturn("boat.insurance.number.unique");

        final Throwable exception = catchThrowable(() -> boatService.createBoat(validBoatDto1));

        assertThat(exception).isInstanceOf(ServiceException.class)
                .hasMessageContaining("boat.insurance.number.unique");
    }

    //UPDATE

    @Test
    public void givenThereAreBoats_whenUpdatingABoatWithValidNewValues_thenBoatIsUpdatedWithTheNewValues() {
        Boat validBoat1 = BoatBuilder.aBoatWithValidFields1().build();
        BoatDto validBoatDto1 = BoatDtoBuilder.aBoatWithValidFields1().build();

        when(boatRepository.findById(validBoat1.getId())).thenReturn(Optional.of(validBoat1));
        when(boatRepository.findBoatByInsurance(validBoatDto1.getInsuranceNumber())).thenReturn(null);
        when(boatRepository.save(any())).thenReturn(validBoat1);

        Boat updatedBoat = boatService.updateBoat(validBoat1.getId(), validBoatDto1);

        assertThat(validBoat1).isSameAs(updatedBoat);
        assertThat(updatedBoat.getName()).isEqualTo(validBoatDto1.getName());
        assertThat(updatedBoat.getEmail()).isEqualTo(validBoatDto1.getEmail());
        assertThat(updatedBoat.getInsuranceNumber()).isEqualTo(validBoatDto1.getInsuranceNumber());
        assertThat(updatedBoat.getLength()).isEqualTo(validBoatDto1.getLength());
        assertThat(updatedBoat.getWidth()).isEqualTo(validBoatDto1.getWidth());
        assertThat(updatedBoat.getHeight()).isEqualTo(validBoatDto1.getHeight());
    }

    @Test
    public void givenThereAreBoats_whenUpdatingABoatWithNonExistentId_thenAnErrorIsReturned() {
        Boat nonExistingBoat = BoatBuilder.aBoatWithValidFields1().build();
        BoatDto validBoatDto1 = BoatDtoBuilder.aBoatWithValidFields1().build();

        when(boatRepository.findById(nonExistingBoat.getId())).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("no.boat.with.this.id"), any(), any())).thenReturn("no.boat.with.this.id");

        final Throwable exception = catchThrowable(() -> boatService.updateBoat(nonExistingBoat.getId(), validBoatDto1));

        assertThat(exception).isInstanceOf(ServiceException.class)
                .hasMessageContaining("no.boat.with.this.id");

    }

    //DELETE

    //SEARCH BY INSURANCE

    @Test
    public void givenThereAreBoats_whenSearchingBoatsByInsuranceNumber_thenBoatWithMatchingIdIsReturned() {
        Boat validBoat1 = BoatBuilder.aBoatWithValidFields1().build();
        String insurance = "0123456789";

        when(boatRepository.findBoatByInsurance(insurance)).thenReturn(validBoat1);

        Boat foundBoat = boatService.getBoatByInsurance(insurance);

        assertThat(foundBoat.getName()).isEqualTo(validBoat1.getName());
        assertThat(foundBoat.getEmail()).isEqualTo(validBoat1.getEmail());
        assertThat(foundBoat.getInsuranceNumber()).isEqualTo(validBoat1.getInsuranceNumber());
        assertThat(foundBoat.getLength()).isEqualTo(validBoat1.getLength());
        assertThat(foundBoat.getWidth()).isEqualTo(validBoat1.getWidth());
        assertThat(foundBoat.getHeight()).isEqualTo(validBoat1.getHeight());
    }

    @Test
    public void givenThereAreNoBoats_whenSearchingBoatsByInsuranceNumber_thenAnErrorIsReturned() {
        String insurance = "0123456789";

        when(messageSource.getMessage(eq("no.boat.insurance.found"), any(), any())).thenReturn("no.boat.insurance.found");

        final Throwable exception = catchThrowable(() -> boatService.getBoatByInsurance(insurance));

        assertThat(exception).isInstanceOf(ServiceException.class)
                .hasMessageContaining("no.boat.insurance.found");
    }

    //SEARCH BY HEIGHT AND WIDTH

    @Test
    public void givenThereAreBoats_whenSearchingBoatsByHeightAndWidth_thenBoatsWithMatchingHeightAndWidthAreReturned() {
        Boat validBoat1 = BoatBuilder.aBoatWithValidFields1().build();
        Boat validBoat2 = BoatBuilder.aBoatWithValidFields2().build();

        int height = 5;
        int width = 5;

        when(boatRepository.findBoatByHeightWidth(height, width)).thenReturn(List.of(validBoat1, validBoat2));
        List<Boat> boats = boatService.getBoatByHeightWidth(height, width);

        assertThat(boats).isNotEmpty();
        assertThat(boats).hasSize(2);
        assertThat(boats).contains(validBoat1, validBoat2);
        assertThat(boats).isEqualTo(List.of(validBoat1, validBoat2));
    }

    @Test
    public void givenThereAreNoBoats_whenSearchingBoatsByHeightAndWidth_thenAnEmptyListIsReturned() {
        int heigth = 5;
        int width = 5;

        when(messageSource.getMessage(eq("no.height.width.found"), any(), any())).thenReturn("no.height.width.found");

        final Throwable exception = catchThrowable(() -> boatService.getBoatByHeightWidth(heigth, width));

        assertThat(exception).isInstanceOf(ServiceException.class)
                .hasMessageContaining("no.height.width.found");
    }

}