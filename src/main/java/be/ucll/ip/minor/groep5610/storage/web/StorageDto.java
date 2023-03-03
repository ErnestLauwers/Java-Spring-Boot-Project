package be.ucll.ip.minor.groep5610.storage.web;

import jakarta.validation.constraints.*;

public class StorageDto {

    private long id;
    private String name;
    private Integer postalCode;
    private Integer space;
    private Integer height;

    public long getId() {
        return id;
    }

    @Size(min = 5, message = "name.short")
    public String getName() {
        return name;
    }

    @Min(value = 1000, message = "postalCode.invalid")
    @Max(value = 9992, message = "postalCode.invalid")
    @NotNull(message = "postalCode.null")
    public Integer getPostalCode() {
        return postalCode;
    }

    @Min(value = 1, message = "space.short")
    @NotNull(message = "space.null")
    public Integer getSpace() {
        return space;
    }

    @Min(value = 1, message = "height.short")
    @NotNull(message = "height.null")
    public Integer getHeight() {
        return height;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public void setSpace(Integer space) {
        this.space = space;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

}