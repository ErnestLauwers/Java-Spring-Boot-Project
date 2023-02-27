package be.ucll.ip.minor.groep5610.storage.web;

import jakarta.validation.constraints.*;

public class StorageDto {

    private long id;

    @NotBlank(message = "name.missing")
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @Pattern(regexp = "\\d{4,}", message = "Postal code must be at least 4 digits long")
    private int postalcode;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    @Min(value = 1, message = "Height must be at least 1")
    private int height;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(int postalcode) {
        this.postalcode = postalcode;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}