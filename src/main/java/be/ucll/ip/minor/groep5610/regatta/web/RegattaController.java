package be.ucll.ip.minor.groep5610.regatta.web;

import be.ucll.ip.minor.groep5610.regatta.domain.Regatta;
import be.ucll.ip.minor.groep5610.regatta.domain.RegattaService;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/regatta/add")
    public String add(Model model) {
        model.addAttribute("regattaDto", new RegattaDto());
        return "regatta/add";
    }

    @PostMapping("/regatta/add")
    public String add(@Valid RegattaDto regatta, BindingResult result, Model model){
        try {
            if(result.hasErrors()) {
                model.addAttribute("regattaDto", regatta);
                return "regatta/add";
            }
            regattaService.createRegatta(regatta);
            return "redirect:/regatta/overview";
        } catch (IllegalArgumentException exc) {
            model.addAttribute("error", exc.getMessage());
            return "regatta/add";
        }
    }

    @GetMapping("/regatta/deleteConfirmation/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        Regatta regatta = regattaService.getRegatta(id);
        model.addAttribute("regatta", toDto(regatta));
        return "regatta/delete-confirmation";
    }

    @PostMapping("/regatta/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        regattaService.deleteRegatta(id);
        return "redirect:/regatta/overview";
    }

    @GetMapping("/regatta/update/{id}")
    public String update(@PathVariable("id") long id, Model model){
        Regatta regatta = regattaService.getRegatta(id);
        model.addAttribute("regattaDto", toDto(regatta));
        return "regatta/update";
    }

    @PostMapping("/regatta/update/{id}")
    public String update(@PathVariable("id") long id, @Valid RegattaDto dto, BindingResult result, Model model) {
        Regatta regatta = regattaService.getRegatta(id);
        try {
            if(result.hasErrors()) {
                model.addAttribute("regatta", toDto(regatta));
                return "regatta/update";
            }
            regattaService.updateRegatta(dto, regatta);
            return "redirect:/regatta/overview";
        } catch (IllegalArgumentException exc) {
            model.addAttribute("error", exc.getMessage());
            return "regatta/update";
        }
    }

    @GetMapping("/regatta/sort/name")
    public String orderByName(Model model){
        List<Regatta> regattasByName = regattaService.sortByName();

        model.addAttribute("regattas", regattasByName);
        return "regatta/overview";
    }

    @GetMapping("/regatta/sort/date")
    public String orderByDate(Model model){
        List<Regatta> regattasByDate = regattaService.sortByDate();

        model.addAttribute("regattas", regattasByDate);
        return "regatta/overview";
    }

    public RegattaDto toDto(Regatta regatta) {
        RegattaDto regattaDto = new RegattaDto();
        regattaDto.setId(regatta.getId());
        regattaDto.setWedstrijdNaam(regatta.getWedstrijdNaam());
        regattaDto.setClubNaam(regatta.getClubNaam());
        regattaDto.setDatum(regatta.getDatum());
        regattaDto.setMaxTeams(regatta.getMaxTeams());
        regattaDto.setCategorie(regatta.getCategorie());
        return regattaDto;
    }

    private void createSampleData() {
        RegattaDto regatta1 = new RegattaDto();
        regatta1.setWedstrijdNaam("wedstrijd1");
        regatta1.setClubNaam("d_club1");
        regatta1.setDatum(LocalDate.now());
        regatta1.setMaxTeams(5);
        regatta1.setCategorie("categorie1");

        RegattaDto regatta2 = new RegattaDto();
        regatta2.setWedstrijdNaam("wedstrijd2");
        regatta2.setClubNaam("a_club2");
        regatta2.setDatum(LocalDate.now().plusDays(5));
        regatta2.setMaxTeams(3);
        regatta2.setCategorie("categorie2");

        RegattaDto regatta3 = new RegattaDto();
        regatta3.setWedstrijdNaam("wedstrijd2");
        regatta3.setClubNaam("g_club2");
        regatta3.setDatum(LocalDate.now().plusDays(1));
        regatta3.setMaxTeams(3);
        regatta3.setCategorie("categorie2");

        regattaService.createRegatta(regatta1);
        regattaService.createRegatta(regatta2);
        regattaService.createRegatta(regatta3);
    }
}
