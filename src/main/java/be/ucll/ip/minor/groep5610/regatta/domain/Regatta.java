package be.ucll.ip.minor.groep5610.regatta.domain;

import be.ucll.ip.minor.groep5610.team.domain.Team;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "regatta")
public class Regatta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany
    @JoinTable(
            name = "registered_teams",
            joinColumns = @JoinColumn(name = "regatta_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private Set<Team> registeredTeams;

    // naam wedstrijd
    private String wedstrijdNaam;

    // naam club
    private String name;

    // datum
    private LocalDate date;

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

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
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

    public void setName(String clubNaam) {
        if(clubNaam.isEmpty() || clubNaam == null) throw new IllegalArgumentException();
        this.name = clubNaam;
    }

    public void setDate(LocalDate datum) {
        if(datum.isBefore(LocalDate.now()) || datum == null) throw new IllegalArgumentException();
        this.date = datum;
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
