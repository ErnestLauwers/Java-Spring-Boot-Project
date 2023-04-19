package be.ucll.ip.minor.groep5610.boat;

import be.ucll.ip.minor.groep5610.Groep5610Application;
import be.ucll.ip.minor.groep5610.boat.domain.Boat;
import be.ucll.ip.minor.groep5610.boat.domain.BoatService;
import be.ucll.ip.minor.groep5610.boat.web.BoatDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.print.attribute.standard.Media;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Groep5610Application.class)
@AutoConfigureMockMvc
public class BoatRestControllerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private BoatService service;

    @Autowired
    private MockMvc boatRestController;

    private Boat validBoat1, validBoat2, invalidBoat;

    private BoatDto validBoatDto1, validBoatDto2, invalidBoatdto;

    @BeforeEach
    public void setUp() {
        validBoat1 = BoatBuilder.aBoatWithValidFields1().build();
        validBoat2 = BoatBuilder.aBoatWithValidFields2().build();
        invalidBoat = BoatBuilder.aBoatWithInvalidFields().build();
        validBoatDto1 = BoatDtoBuilder.aBoatWithValidFields1().build();
        validBoatDto2 = BoatDtoBuilder.aBoatWithValidFields2().build();
        invalidBoatdto = BoatDtoBuilder.aBoatWithInvalidFields().build();
    }

    //OVERVIEW

    @Test
    public void givenThereAreBoats_whenGetRequestSendToGetAllBoats_thenJSONWithAllBoatsIsReturned() throws Exception {
        List<Boat> boats = Arrays.asList(validBoat1, validBoat2);

        given(service.getAllBoats()).willReturn(boats);

        boatRestController.perform(get("/api/boat/overview")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Is.is(validBoatDto1.getName())))
                .andExpect(jsonPath("$[0].email", Is.is(validBoatDto1.getEmail())))
                .andExpect(jsonPath("$[0].insuranceNumber", Is.is(validBoatDto1.getInsuranceNumber())))
                .andExpect(jsonPath("$[0].length", Is.is(validBoatDto1.getLength())))
                .andExpect(jsonPath("$[0].width", Is.is(validBoatDto1.getWidth())))
                .andExpect(jsonPath("$[0].height", Is.is(validBoatDto1.getHeight())))
                .andExpect(jsonPath("$[1].name", Is.is(validBoatDto2.getName())))
                .andExpect(jsonPath("$[1].email", Is.is(validBoatDto2.getEmail())))
                .andExpect(jsonPath("$[1].insuranceNumber", Is.is(validBoatDto2.getInsuranceNumber())))
                .andExpect(jsonPath("$[1].length", Is.is(validBoatDto2.getLength())))
                .andExpect(jsonPath("$[1].width", Is.is(validBoatDto2.getWidth())))
                .andExpect(jsonPath("$[1].height", Is.is(validBoatDto2.getHeight())));
    }

    @Test
    public void givenThereAreNoBoats_whenGetRequestSendToGetAllBoats_thenEmptyJSONIsReturned() throws Exception {
        given(service.getAllBoats()).willReturn(Collections.emptyList());

        boatRestController.perform(get("/api/boat/overview")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }

    //ADD

    @Test
    public void givenThereAreNoBoats_whenPostRequestSendToAddValidBoat_thenJSONWithAddedBoatIsReturned() throws Exception {
        given(service.createBoat(any())).willReturn(validBoat1);

        boatRestController.perform(post("/api/boat/add")
                        .content(mapper.writeValueAsString(validBoatDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is(validBoatDto1.getName())))
                .andExpect(jsonPath("$.email", Is.is(validBoatDto1.getEmail())))
                .andExpect(jsonPath("$.insuranceNumber", Is.is(validBoatDto1.getInsuranceNumber())))
                .andExpect(jsonPath("$.length", Is.is(validBoatDto1.getLength())))
                .andExpect(jsonPath("$.width", Is.is(validBoatDto1.getWidth())))
                .andExpect(jsonPath("$.height", Is.is(validBoatDto1.getHeight())));
    }

    @Test
    public void givenThereAreNoBoats_whenPostRequestSendToAddInvalidBoat_thenErrorsInJSONFormatAreReturned() throws Exception {
        boatRestController.perform(post("/api/boat/add")
                        .content(mapper.writeValueAsString(invalidBoatdto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is("Name needs to be at least 5 characters long")))
                .andExpect(jsonPath("$.email", Is.is("The email linked with the boat cannot be blank")))
                .andExpect(jsonPath("$.insuranceNumber", Is.is("The Insurance Number must be 10 characters long")))
                .andExpect(jsonPath("$.length", Is.is("The Length of the boat must be positive")))
                .andExpect(jsonPath("$.width", Is.is("The width must be positive")))
                .andExpect(jsonPath("$.height", Is.is("The height must be positive")));
    }

    //UPDATE

    @Test
    public void givenThereAreBoats_whenPutRequestSendToUpdateABoatWithValidValues_thenJSONWithUpdatedBoatIsReturned() throws Exception {
        given(service.updateBoat(eq(validBoat1.getId()), any())).willReturn(validBoat2);

        boatRestController.perform(put("/api/boat/update?id=" + validBoat1.getId())
                        .content(mapper.writeValueAsString(validBoatDto2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is(validBoatDto2.getName())))
                .andExpect(jsonPath("$.email", Is.is(validBoatDto2.getEmail())))
                .andExpect(jsonPath("$.insuranceNumber", Is.is(validBoatDto2.getInsuranceNumber())))
                .andExpect(jsonPath("$.length", Is.is(validBoatDto2.getLength())))
                .andExpect(jsonPath("$.width", Is.is(validBoatDto2.getWidth())))
                .andExpect(jsonPath("$.height", Is.is(validBoatDto2.getHeight())));
    }

    @Test
    public void givenThereAreNoBoats_whenPutRequestSendToUpdateABoatWithNonExistentId_thenErrorInJSONFormatIsReturned() throws Exception {
        given(service.updateBoat(eq(validBoat1.getId()), any())).willThrow(new ServiceException("No boat found with this id."));

        boatRestController.perform(put("/api/boat/update?id=" + validBoat1.getId())
                        .content(mapper.writeValueAsString(validBoatDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", Is.is("No boat found with this id.")));
    }

    //DELETE

    @Test
    public void givenThereAreBoats_whenDeleteRequestSendToDeleteABoatWithValidId_thenJSONWithDeletedBoatIsReturned() throws Exception {
        given(service.getBoat(validBoat1.getId())).willReturn(validBoat1);

        boatRestController.perform(delete("/api/boat/delete?id=" + validBoat1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is(validBoat1.getName())))
                .andExpect(jsonPath("$.email", Is.is(validBoat1.getEmail())))
                .andExpect(jsonPath("$.insuranceNumber", Is.is(validBoat1.getInsuranceNumber())))
                .andExpect(jsonPath("$.length", Is.is(validBoat1.getLength())))
                .andExpect(jsonPath("$.width", Is.is(validBoat1.getWidth())))
                .andExpect(jsonPath("$.height", Is.is(validBoat1.getHeight())));

    }

    @Test
    public void givenThereAreNoBoats_whenDeleteRequestSendToDeleteABoatWithNonExistentId_thenErrorInJSONFormatIsReturned() throws Exception {
        given(service.getBoat(123L)).willThrow(new ServiceException("No boat found with this id."));

        boatRestController.perform(delete("/api/boat/delete?id=" + 123)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", Is.is("No boat found with this id.")));
    }

    //SEARCH BY INSURANCE

    @Test
    public void givenThereAreBoats_whenGetRequestSendToSearchByInsuranceNumber_thenJSONWithTheBoatThatMatchesIsReturned() throws Exception {
        Boat boat = validBoat1;
        String insurance = "0123456789";

        given(service.getBoatByInsurance(insurance)).willReturn(boat);

        boatRestController.perform(get("/api/boat/search")
                .param("insurance", insurance)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Is.is(validBoat1.getName())))
                .andExpect(jsonPath("$.email", Is.is(validBoat1.getEmail())))
                .andExpect(jsonPath("$.insuranceNumber", Is.is(validBoat1.getInsuranceNumber())))
                .andExpect(jsonPath("$.length", Is.is(validBoat1.getLength())))
                .andExpect(jsonPath("$.width", Is.is(validBoat1.getWidth())))
                .andExpect(jsonPath("$.height", Is.is(validBoat1.getHeight())));


    }

    @Test
    public void givenThereAreBoats_whenGetRequestSendToSearchByInsuranceNumberThatDoesntExist_thenErrorInJSONIsReturned() throws Exception {
        String insurance = "982437817";

        given(service.getBoatByInsurance(insurance)).willThrow(new ServiceException("There is no boat with this insurance number"));

        boatRestController.perform(get("/api/boat/search")
                        .param("insurance", insurance)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Is.is("There is no boat with this insurance number")));
    }

    //SEARCH BY HEIGHT AND WIDTH

    @Test
    public void givenThereAreBoats_whenGetRequestSendToSearchByHeightAndWidth_thenJSONWithMatchingBoatIsReturned() throws Exception {
        List<Boat> boats = Arrays.asList(validBoat1, validBoat2);
        int height = 5;
        int width = 5;

        given(service.getBoatByHeightWidth(height, width)).willReturn(boats);

        boatRestController.perform(get("/api/boat/search/{height}/{width}", height, width)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(validBoat1.getName())))
                .andExpect(jsonPath("$[0].email", Is.is(validBoat1.getEmail())))
                .andExpect(jsonPath("$[0].insuranceNumber", Is.is(validBoat1.getInsuranceNumber())))
                .andExpect(jsonPath("$[0].length", Is.is(validBoat1.getLength())))
                .andExpect(jsonPath("$[0].width", Is.is(validBoat1.getWidth())))
                .andExpect(jsonPath("$[0].height", Is.is(validBoat1.getHeight())))
                .andExpect(jsonPath("$[1].name", Is.is(validBoat2.getName())))
                .andExpect(jsonPath("$[1].email", Is.is(validBoat2.getEmail())))
                .andExpect(jsonPath("$[1].insuranceNumber", Is.is(validBoat2.getInsuranceNumber())))
                .andExpect(jsonPath("$[1].length", Is.is(validBoat2.getLength())))
                .andExpect(jsonPath("$[1].width", Is.is(validBoat2.getWidth())))
                .andExpect(jsonPath("$[1].height", Is.is(validBoat2.getHeight())));
    }

    @Test
    public void givenThereAreNoBoats_whenGetRequestSendToSearchByHeightAndWidth_thenEmptyJSONIsReturned() throws Exception {
        int height = 5;
        int width = 5;

        given(service.getBoatByHeightWidth(height, width)).willReturn(Collections.emptyList());

        boatRestController.perform(get("/api/boat/search/{height}/{width}", height, width)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

}