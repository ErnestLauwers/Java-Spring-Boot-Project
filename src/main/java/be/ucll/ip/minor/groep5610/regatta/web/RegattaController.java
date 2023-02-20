package be.ucll.ip.minor.groep5610.regatta.web;

import be.ucll.ip.minor.groep5610.regatta.domain.Regatta;
import be.ucll.ip.minor.groep5610.regatta.domain.RegattaService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class RegattaController {

    private static Logger LOGGER = LoggerFactory.getLogger(RegattaController.class);


    private final RegattaService regattaService;

    @Autowired
    public RegattaController(RegattaService regattaService) {
        this.regattaService = regattaService;
    }

    @GetMapping("/home")
    public String index(){
        return "index";
    }

    @GetMapping("/regatta/overview")
    public String overview(Model model){
        List<Regatta> allRegattas = regattaService.getRegattas();

        if (allRegattas.isEmpty()) {
            createSampleData();
            allRegattas = regattaService.getRegattas();
        }

        model.addAttribute("regattas", allRegattas);
        return "regatta/overview";
    }

    private void createSampleData() {
        RegattaDto regatta1 = new RegattaDto();
        regatta1.setWedstrijdNaam("wedstrijd1");
        regatta1.setClubNaam("club1");
        regatta1.setDatum(LocalDate.parse("2023-02-20"));
        regatta1.setMaxTeams(5);
        regatta1.setCategorie("categorie1");

        RegattaDto regatta2 = new RegattaDto();
        regatta2.setWedstrijdNaam("wedstrijd2");
        regatta2.setClubNaam("club2");
        regatta2.setDatum(LocalDate.parse("2023-02-25"));
        regatta2.setMaxTeams(3);
        regatta2.setCategorie("categorie2");

        regattaService.createRegatta(regatta1);
        regattaService.createRegatta(regatta2);
    }
}
