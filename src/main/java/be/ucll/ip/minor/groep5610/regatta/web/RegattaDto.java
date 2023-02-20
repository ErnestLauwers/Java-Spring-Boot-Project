package be.ucll.ip.minor.groep5610.regatta.web;


import java.time.LocalDate;

public class RegattaDto {
    private long id;
    private String wedstrijdNaam;
    private String clubNaam;
    private LocalDate datum;
    private int maxTeams;
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

    public int getMaxTeams() {
        return maxTeams;
    }

    public String getCategorie() {
        return categorie;
    }

    // SETTERS

    public void setId(long id) {
        this.id = id;
    }

    public void setWedstrijdNaam(String wedstrijdnaam) {
        this.wedstrijdNaam = wedstrijdnaam;
    }

    public void setClubNaam(String clubNaam) {
        this.clubNaam = clubNaam;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public void setMaxTeams(int maxTeams) {
        this.maxTeams = maxTeams;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
}
