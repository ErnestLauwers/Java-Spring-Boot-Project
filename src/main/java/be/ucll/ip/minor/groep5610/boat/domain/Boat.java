package be.ucll.ip.minor.groep5610.boat.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "boat")
public class Boat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String email;

    private Integer length;

    private Integer width;

    private Integer height;

    private String insuranceNumber;


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank() || name.length() < 5) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException();
        }
        this.email = email;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        if (length <= 0) {
            throw new IllegalArgumentException();
        }
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        if (width <= 0) {
            throw new IllegalArgumentException();
        }
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        if (height <= 0) {
            throw new IllegalArgumentException();
        }
        this.height = height;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        if (insuranceNumber == null || insuranceNumber.isBlank() || insuranceNumber.length() > 10) {
            throw new IllegalArgumentException();
        }
        this.insuranceNumber = insuranceNumber;
    }
}
