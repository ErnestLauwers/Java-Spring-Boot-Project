package be.ucll.ip.minor.groep5610.team.web;

import be.ucll.ip.minor.groep5610.regatta.web.RegattaTeamDto;
import jakarta.validation.constraints.*;

import java.util.List;

public class TeamDto {
    private long id;
    private String name;
    private String category;
    private Integer passengers;
    private String club;
    private List<RegattaTeamDto> registeredIn;

    //GETTERS
    public long getId() {
        return id;
    }

    @Size(min = 5, message = "{team.name.invalid}")
    public String getName() {
        return name;
    }

    @Pattern(regexp = "^[a-zA-Z0-9]{7}$", message = "{team.category.invalid}")
    public String getCategory() {
        return category;
    }

    @NotNull(message = "{team.number.of.passengers.missing}")
    @Min(value = 1, message = "{team.number.of.passengers.invalid}")
    @Max(value = 12, message = "{team.number.of.passengers.invalid}")
    public Integer getPassengers() {
        return passengers;
    }

    public String getClub() {
        return club;
    }

    public List<RegattaTeamDto> getRegisteredIn() {
        return registeredIn;
    }

    //SETTERS
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPassengers(Integer passengers) {
        this.passengers = passengers;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public void setRegisteredIn(List<RegattaTeamDto> registeredIn) {
        this.registeredIn = registeredIn;
    }
}
