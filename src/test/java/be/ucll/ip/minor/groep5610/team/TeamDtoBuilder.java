package be.ucll.ip.minor.groep5610.team;

import be.ucll.ip.minor.groep5610.team.web.TeamDto;

public class TeamDtoBuilder {
    private String name;
    private String category;
    private Integer passengers;
    private String club;

    private TeamDtoBuilder() {

    }

    public static TeamDtoBuilder aTeam() {
        return new TeamDtoBuilder();
    }

    public static TeamDtoBuilder aValidTeamAlpha() {
        return aTeam().withName("Alpha").withCategory("AbCd123").withPassengers(5).withClub("ClubA");
    }

    public static TeamDtoBuilder aTeamWithNoName() {
        return aTeam().withCategory("AbCd123").withPassengers(5).withClub("ClubA");
    }

    public static TeamDtoBuilder aTeamWithNoCategory() {
        return aTeam().withName("Alpha").withPassengers(5).withClub("ClubA");
    }

    public static TeamDtoBuilder aTeamWithNoPassengers() {
        return aTeam().withName("Alpha").withCategory("AbCd123").withClub("ClubA");
    }

    public static TeamDtoBuilder aTeamWithNoClub() {
        return aTeam().withName("Alpha").withCategory("AbCd123").withPassengers(5);
    }

    public TeamDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TeamDtoBuilder withCategory(String category) {
        this.category = category;
        return this;
    }

    public TeamDtoBuilder withPassengers(int passengers) {
        this.passengers = passengers;
        return this;
    }

    public TeamDtoBuilder withClub(String club) {
        this.club = club;
        return this;
    }

    public TeamDto build() {
        TeamDto team = new TeamDto();
        team.setName(name);
        team.setCategory(category);
        team.setPassengers(passengers);
        team.setClub(club);
        return team;
    }
}
