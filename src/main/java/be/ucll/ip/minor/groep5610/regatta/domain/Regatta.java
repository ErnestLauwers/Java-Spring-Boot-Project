package be.ucll.ip.minor.groep5610.regatta.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "regatta")
public class Regatta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // naam wedstrijd
    private String wedstrijdNaam;

    // naam club
    private String clubNaam;

    // datum
    private LocalDate datum;

    // max teams
    private Integer maxTeams;

    // categorie
    private String categorie;

    // GETTERS

    public long getId() {
        return id;
    }

    public String getWedstrijdNaam() {
        return wedstrijdNaam;
    }

    public String getClubNaam() {
        return clubNaam;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public Integer getMaxTeams() {
        return maxTeams;
    }

    public String getCategorie() {
        return categorie;
    }

    // SETTERS

    public void setId(long id) {
        this.id = id;
    }

    public void setWedstrijdNaam(String wedstrijdNaam) {
        if(wedstrijdNaam.isEmpty() || wedstrijdNaam == null) throw new IllegalArgumentException();
        this.wedstrijdNaam = wedstrijdNaam;
    }

    public void setClubNaam(String clubNaam) {
        if(clubNaam.isEmpty() || clubNaam == null) throw new IllegalArgumentException();
        this.clubNaam = clubNaam;
    }

    public void setDatum(LocalDate datum) {
        if(datum.isBefore(LocalDate.now()) || datum == null) throw new IllegalArgumentException();
        this.datum = datum;
    }

    public void setMaxTeams(Integer maxTeams) {
        if(maxTeams < 0) throw new IllegalArgumentException();
        this.maxTeams = maxTeams;
    }

    public void setCategorie(String categorie) {
        if(categorie.isEmpty() || categorie == null) throw new IllegalArgumentException();
        this.categorie = categorie;
    }
}
