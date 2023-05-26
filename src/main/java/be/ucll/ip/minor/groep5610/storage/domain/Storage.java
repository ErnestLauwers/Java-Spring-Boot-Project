package be.ucll.ip.minor.groep5610.storage.domain;

import be.ucll.ip.minor.groep5610.boat.domain.Boat;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "storage")
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "storage")
    private List<Boat> boats;

    private String name;

    private Integer postalCode;

    private Integer space;

    private Integer height;

    public List<Boat> getBoats() {
        return boats;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public Integer getSpace() {
        return space;
    }

    public Integer getHeight() {
        return height;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.isBlank() || name.length() < 5) throw new IllegalArgumentException();
        this.name = name;
    }

    public void setPostalCode(Integer postalCode) {
        if (postalCode < 1000 || postalCode > 9992) throw new IllegalArgumentException();
        this.postalCode = postalCode;
    }

    public void setSpace(int space) {
        if (space < 1) throw new IllegalArgumentException();
        this.space = space;
    }

    public void setHeight(int height) {
        if (height < 1) throw new IllegalArgumentException();
        this.height = height;
    }

    public void addBoat(Boat boat) {
        this.getBoats().add(boat);
        boat.setStorage(this);
    }

    public void removeBoat(Boat boat) {
        this.getBoats().remove(boat);
        boat.setStorage(null);
    }
}