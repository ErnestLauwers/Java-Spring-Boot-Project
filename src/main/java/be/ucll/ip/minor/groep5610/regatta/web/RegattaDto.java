package be.ucll.ip.minor.groep5610.regatta.web;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public class RegattaDto {
    private long id;
    private List<RegattaTeamDto> registeredTeams;
    private String wedstrijdNaam;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Integer maxTeams;
    private String categorie;

    // GETTERS

    public long getId() {
        return id;
    }

    public List<RegattaTeamDto> getRegisteredTeams() {
        return registeredTeams;
    }

    @NotBlank(message = "wedstrijd.naam.missing")
    public String getWedstrijdNaam() {
        return wedstrijdNaam;
    }

    @NotBlank(message = "club.naam.missing")
    public String getName() {
        return name;
    }

    @FutureOrPresent(message = "date.in.past")
    @NotNull(message = "date.not.null")
    public LocalDate getDate() {
        return date;
    }

    @NotNull(message = "teams.not.null")
    @Positive(message = "teams.must.be.positive")
    public Integer getMaxTeams() {
        return maxTeams;
    }

    @NotBlank(message = "categorie.missing")
    public String getCategorie() {
        return categorie;
    }

    // SETTERS

    public void setId(long id) {
        this.id = id;
    }

    public void setRegisteredTeams(List<RegattaTeamDto> registeredTeams) {
        this.registeredTeams = registeredTeams;
    }

    public void setWedstrijdNaam(String wedstrijdnaam) {
        this.wedstrijdNaam = wedstrijdnaam;
    }

    public void setName(String clubNaam) {
        this.name = clubNaam;
    }

    public void setDate(LocalDate datum) {
        this.date = datum;
    }

    public void setMaxTeams(Integer maxTeams) {
        this.maxTeams = maxTeams;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
}
