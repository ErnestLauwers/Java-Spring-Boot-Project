package be.ucll.ip.minor.groep5610.team.domain;

import be.ucll.ip.minor.groep5610.regatta.domain.Regatta;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(mappedBy = "registeredTeams")
    private Set<Regatta> registeredIn;

    private String name;
    private String category;
    private Integer passengers;
    private String club;

    //GETTERS
    public long getId() {
        return id;
    }

    public Set<Regatta> getRegisteredIn() {
        if (registeredIn == null) {
            registeredIn = new HashSet<>();
        }
        return registeredIn;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Integer getPassengers() {
        return passengers;
    }

    public String getClub() {
        return club;
    }

    //SETTERS
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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

    public void addRegisteredIn(Regatta regatta) {
        this.getRegisteredIn().add(regatta);
    }

    public boolean isAlreadyRegisteredInRegattaOnDate(LocalDate date) {
        for (Regatta regatta: this.registeredIn) {
            if (regatta.getDate().equals(date)) {
                return true;
            }
        }
        return false;
    }
}
