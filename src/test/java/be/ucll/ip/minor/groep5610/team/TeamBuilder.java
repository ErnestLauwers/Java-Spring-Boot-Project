package be.ucll.ip.minor.groep5610.team;

import be.ucll.ip.minor.groep5610.team.domain.Team;

public class TeamBuilder {
    private long id;
    private String name;
    private String category;
    private Integer passengers;
    private String club;

    private TeamBuilder() {
    }

    public static TeamBuilder aTeam() {
        return new TeamBuilder();
    }

    public static TeamBuilder aValidTeamAlpha() {
        return aTeam().withId(1).withName("Alpha").withCategory("AbCd123").withPassengers(5).withClub("ClubA");
    }

    public static TeamBuilder aValidTeamDelta() {
        return aTeam().withId(2).withName("Delta").withCategory("qztg581").withPassengers(4).withClub("ClubB");
    }

    public static TeamBuilder aTeamWithNoName() {
        return aTeam().withCategory("AbCd123").withPassengers(5).withClub("ClubA");
    }

    public static TeamBuilder aTeamWithNoCategory() {
        return aTeam().withName("Alpha").withPassengers(5).withClub("ClubA");
    }

    public static TeamBuilder aTeamWithNoPassengers() {
        return aTeam().withName("Alpha").withCategory("AbCd123").withClub("ClubA");
    }

    public static TeamBuilder aTeamWithNoClub() {
        return aTeam().withName("Alpha").withCategory("AbCd123").withPassengers(5);
    }

    public TeamBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public TeamBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TeamBuilder withCategory(String category) {
        this.category = category;
        return this;
    }

    public TeamBuilder withPassengers(int passengers) {
        this.passengers = passengers;
        return this;
    }

    public TeamBuilder withClub(String club) {
        this.club = club;
        return this;
    }

    public Team build() {
        Team team = new Team();
        team.setId(id);
        team.setName(name);
        team.setCategory(category);
        team.setPassengers(passengers);
        team.setClub(club);
        return team;
    }
}
