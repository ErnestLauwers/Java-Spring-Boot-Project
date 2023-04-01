package be.ucll.ip.minor.groep5610.team;

import be.ucll.ip.minor.groep5610.team.web.TeamDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TeamDtoTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void givenValidTeam_shouldHaveNoViolations() {
        //given
        TeamDto teamAlpha = TeamDtoBuilder.aValidTeamAlpha().build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(teamAlpha);

        //then
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenTeamWithNoClub_shouldHaveNoViolations() {
        //given
        TeamDto teamAlpha = TeamDtoBuilder.aTeamWithNoClub().build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(teamAlpha);

        //then
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenNameWithLessThanFiveCharacters_shouldDetectInvalidNameError() {
        //given
        TeamDto teamBeta = TeamDtoBuilder.aTeamWithNoName().withName("Beta").build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(teamBeta);

        //then
        assertEquals(violations.size(), 1);
        ConstraintViolation<TeamDto> violation = violations.iterator().next();
        assertEquals("team.name.short", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("Beta", violation.getInvalidValue());
    }

    @Test
    public void givenTeamWithEmptyName_shouldDetectInvalidNameError() {
        //given
        TeamDto teamAlpha = TeamDtoBuilder.aTeamWithNoName().withName("").build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(teamAlpha);

        //then
        assertEquals(violations.size(), 1);
        ConstraintViolation<TeamDto> violation = violations.iterator().next();
        assertEquals("team.name.missing", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    public void givenCategoryWithNonAlphanumericCharacters_shouldDetectInvalidCategoryError() {
        //given
        TeamDto teamAlpha = TeamDtoBuilder.aTeamWithNoCategory().withCategory("ABCD1?!").build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(teamAlpha);

        //then
        assertEquals(violations.size(), 1);
        ConstraintViolation<TeamDto> violation = violations.iterator().next();
        assertEquals("team.category.non.alphanumeric", violation.getMessage());
        assertEquals("category", violation.getPropertyPath().toString());
        assertEquals("ABCD1?!", violation.getInvalidValue());
    }

    @Test
    public void givenCategoryWithASequenceOfLessThanSevenCharacters_shouldDetectInvalidCategoryError() {
        //given
        TeamDto teamAlpha = TeamDtoBuilder.aTeamWithNoCategory().withCategory("123456").build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(teamAlpha);

        //then
        assertEquals(violations.size(), 1);
        ConstraintViolation<TeamDto> violation = violations.iterator().next();
        assertEquals("team.category.short", violation.getMessage());
        assertEquals("category", violation.getPropertyPath().toString());
        assertEquals("123456", violation.getInvalidValue());
    }

    @Test
    public void givenCategoryWithASequenceOfMoreThanSevenCharacters_shouldDetectInvalidCategoryError() {
        //given
        TeamDto teamAlpha = TeamDtoBuilder.aTeamWithNoCategory().withCategory("12345678").build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(teamAlpha);

        //then
        assertEquals(violations.size(), 1);
        ConstraintViolation<TeamDto> violation = violations.iterator().next();
        assertEquals("team.category.long", violation.getMessage());
        assertEquals("category", violation.getPropertyPath().toString());
        assertEquals("12345678", violation.getInvalidValue());
    }

    @Test
    public void givenTeamWithEmptyCategory_shouldDetectInvalidCategoryError() {
        //given
        TeamDto teamAlpha = TeamDtoBuilder.aTeamWithNoCategory().withCategory("").build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(teamAlpha);

        //then
        assertEquals(violations.size(), 1);
        ConstraintViolation<TeamDto> violation = violations.iterator().next();
        assertEquals("team.category.missing", violation.getMessage());
        assertEquals("category", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    public void givenPassengersLessThanOne_shouldDetectInvalidPassengersError() {
        //given
        TeamDto teamAlpha = TeamDtoBuilder.aTeamWithNoPassengers().withPassengers(0).build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(teamAlpha);

        //then
        assertEquals(violations.size(), 1);
        ConstraintViolation<TeamDto> violation = violations.iterator().next();
        assertEquals("team.number.of.passengers.low", violation.getMessage());
        assertEquals("passengers", violation.getPropertyPath().toString());
        assertEquals(0, violation.getInvalidValue());
    }

    @Test
    public void givenPassengersGreaterThanTwelve_shouldDetectInvalidPassengersError() {
        //given
        TeamDto teamAlpha = TeamDtoBuilder.aTeamWithNoPassengers().withPassengers(13).build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(teamAlpha);

        //then
        assertEquals(violations.size(), 1);
        ConstraintViolation<TeamDto> violation = violations.iterator().next();
        assertEquals("team.number.of.passengers.high", violation.getMessage());
        assertEquals("passengers", violation.getPropertyPath().toString());
        assertEquals(13, violation.getInvalidValue());
    }

    @Test
    public void givenTeamWithEmptyInputForPassengers_shouldDetectInvalidPassengersError() {
        //given
        TeamDto teamAlpha = TeamDtoBuilder.aTeamWithNoPassengers().build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(teamAlpha);

        //then
        assertEquals(violations.size(), 1);
        ConstraintViolation<TeamDto> violation = violations.iterator().next();
        assertEquals("team.number.of.passengers.missing", violation.getMessage());
        assertEquals("passengers", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }
}
