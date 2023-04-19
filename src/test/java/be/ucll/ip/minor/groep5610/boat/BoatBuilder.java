package be.ucll.ip.minor.groep5610.boat;

import be.ucll.ip.minor.groep5610.boat.domain.Boat;

public class BoatBuilder {

    private long id;
    private String name;
    private String email;
    private String insuranceNumber;
    private int length;
    private int width;
    private int height;

    public BoatBuilder(){}

    public static BoatBuilder aBoat() {
        return new BoatBuilder();
    }

    public static BoatBuilder aBoatWithValidFields1() {
        return aBoat().withid(1).withName("testing1").withEmail("test1@gmail.com").withInsuranceNumber("0123456789").withLength(5).withWidth(5).withHeight(5);
    }

    public static BoatBuilder aBoatWithValidFields2() {
        return aBoat().withid(2).withName("testing2").withEmail("test2@gmail.com").withInsuranceNumber("9876543210").withLength(5).withWidth(5).withHeight(5);
    }

    public static BoatBuilder aBoatWithInvalidFields() {
        return aBoat().withid(3).withName("").withEmail("").withInsuranceNumber("").withLength(0).withWidth(0).withHeight(0);
    }

    public BoatBuilder withid(long id) {
        this.id = id;
        return this;
    }

    public BoatBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public BoatBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public BoatBuilder withInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
        return this;
    }

    public BoatBuilder withLength(int length) {
        this.length = length;
        return this;
    }

    public BoatBuilder withWidth(int width) {
        this.width = width;
        return this;
    }

    public BoatBuilder withHeight(int height) {
        this.height = height;
        return this;
    }

    public Boat build() {
        Boat boat = new Boat();
        boat.setId(id);
        boat.setName(name);
        boat.setEmail(email);
        boat.setLength(length);
        boat.setWidth(width);
        boat.setHeight(height);
        boat.setInsuranceNumber(insuranceNumber);
        return boat;
    }

}