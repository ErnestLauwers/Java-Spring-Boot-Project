package be.ucll.ip.minor.groep5610.boat;

import be.ucll.ip.minor.groep5610.boat.web.BoatDto;

public class BoatDtoBuilder {

    private long id;
    private String name;
    private String email;
    private String insuranceNumber;
    private int length;
    private int width;
    private int height;

    public BoatDtoBuilder(){}

    public static BoatDtoBuilder aBoatDto() {
        return new BoatDtoBuilder();
    }

    public static BoatDtoBuilder aBoatWithValidFields1() {
        return aBoatDto().withId(1).withName("testing1").withEmail("test1@gmail.com").withInsuranceNumber("0123456789").withLength(5).withWidth(5).withHeight(5);
    }

    public static BoatDtoBuilder aBoatWithValidFields2() {
        return aBoatDto().withId(2).withName("testing2").withEmail("test2@gmail.com").withInsuranceNumber("9876543210").withLength(5).withWidth(5).withHeight(5);
    }

    public static BoatDtoBuilder aBoatWithInvalidFields() {
        return aBoatDto().withId(3).withName("").withEmail("").withInsuranceNumber("").withLength(0).withWidth(0).withHeight(0);
    }

    public static BoatDtoBuilder aBoatWithNoName() {
        return aBoatDto().withEmail("test@gmail.com").withInsuranceNumber("0123456789").withLength(5).withWidth(5).withHeight(5);
    }

    public static BoatDtoBuilder aBoatWithNoEmail() {
        return aBoatDto().withName("testing").withInsuranceNumber("0123456789").withLength(5).withWidth(5).withHeight(5);
    }

    public static BoatDtoBuilder aBoatWithNoInsuranceNumber() {
        return aBoatDto().withName("testing").withEmail("test@gmail.com").withLength(5).withWidth(5).withHeight(5);
    }

    public static BoatDtoBuilder aBoatWithNoLength() {
        return aBoatDto().withName("testing").withEmail("test@gmail.com").withInsuranceNumber("0123456789").withWidth(5).withHeight(5);
    }

    public static BoatDtoBuilder aBoatWithNoWidth() {
        return aBoatDto().withName("testing").withEmail("test@gmail.com").withInsuranceNumber("0123456789").withLength(5).withHeight(5);
    }

    public static BoatDtoBuilder aBoatWithNoHeigth() {
        return aBoatDto().withName("testing").withEmail("test@gmail.com").withInsuranceNumber("0123456789").withLength(5).withWidth(5);
    }

    public BoatDtoBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public BoatDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public BoatDtoBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public BoatDtoBuilder withInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
        return this;
    }

    public BoatDtoBuilder withLength(int length) {
        this.length = length;
        return this;
    }

    public BoatDtoBuilder withWidth(int width) {
        this.width = width;
        return this;
    }

    public BoatDtoBuilder withHeight(int height) {
        this.height = height;
        return this;
    }

    public BoatDto build() {
        BoatDto boatDto = new BoatDto();
        boatDto.setId(id);
        boatDto.setName(name);
        boatDto.setEmail(email);
        boatDto.setLength(length);
        boatDto.setWidth(width);
        boatDto.setHeight(height);
        boatDto.setInsuranceNumber(insuranceNumber);
        return boatDto;
    }

}