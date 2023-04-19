package be.ucll.ip.minor.groep5610.regatta.web;

import java.time.LocalDate;

public class RegattaSearchDto {
    private LocalDate dateAfter = LocalDate.of(1000, 1, 1);
    private LocalDate dateBefore = LocalDate.of(9999, 1, 1);
    private String category = "";

    //GETTERS
    public LocalDate getDateAfter() {
        return dateAfter;
    }

    public LocalDate getDateBefore() {
        return dateBefore;
    }

    public String getCategory() {
        return category;
    }

    //SETTERS
    public void setDateAfter(LocalDate dateAfter) {
        if (dateAfter != null) {
            this.dateAfter = dateAfter;
        }
    }

    public void setDateBefore(LocalDate dateBefore) {
        if (dateBefore != null) {
            this.dateBefore = dateBefore;
        }
    }

    public void setCategory(String category) {
        if (category != null) {
            this.category = category;
        }
    }
}
