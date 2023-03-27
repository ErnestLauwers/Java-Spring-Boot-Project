package be.ucll.ip.minor.groep5610.boat.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class BoatDto {

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

    public void setId(long id) {
        this.id = id;
    }

    @Size(min = 5, message = "name.short")
    @NotBlank(message = "boat.name.missing")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank(message = "boat.email.missing")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Positive(message = "length.must.be.positive")
    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Positive(message = "width.must.be.positive")
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Positive(message = "height.must.be.positive")
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Size(max = 10, min = 10, message = "insurance.number.incorrect")
    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }
}
