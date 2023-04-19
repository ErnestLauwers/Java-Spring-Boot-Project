package be.ucll.ip.minor.groep5610.boat;

import be.ucll.ip.minor.groep5610.boat.web.BoatDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class BoatDtoTest {

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
    public void givenBoatWithValidFields_shouldHaveNoViolations() {
        BoatDto boatDto = BoatDtoBuilder.aBoatWithValidFields1().build();
        Set<ConstraintViolation<BoatDto>> violations = validator.validate(boatDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenBoatWithNameThatHasLessThan5Characters_shouldDetectInvalidNameError() {
        BoatDto boatDto = BoatDtoBuilder.aBoatWithNoName().withName("test").build();
        Set< ConstraintViolation<BoatDto>> violations = validator.validate(boatDto);
        assertEquals(violations.size(), 1);
        ConstraintViolation<BoatDto> violation = violations.iterator().next();
        assertEquals("{name.short}", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("test", violation.getInvalidValue());
    }

}