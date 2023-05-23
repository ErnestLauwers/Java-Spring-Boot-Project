package be.ucll.ip.minor.groep5610.boat.web;

import be.ucll.ip.minor.groep5610.storage.domain.Storage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String storageName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStorageName(String name) {
        this.storageName = name;
    }

    public String getStorageName() {
        return this.storageName;
    }

    @Size(min = 5, message = "{name.short}")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank(message = "{boat.email.missing}")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull(message = "{boat.length.missing}")
    @Positive(message = "{length.must.be.positive}")
    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @NotNull(message = "{boat.width.missing}")
    @Positive(message = "{width.must.be.positive}")
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @NotNull(message = "{boat.height.missing}")
    @Positive(message = "{height.must.be.positive}")
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Size(max = 10, min = 10, message = "{insurance.number.incorrect}")
    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }
}
