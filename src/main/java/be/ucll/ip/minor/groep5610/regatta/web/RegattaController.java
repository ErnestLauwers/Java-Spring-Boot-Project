package be.ucll.ip.minor.groep5610.regatta.web;

import be.ucll.ip.minor.groep5610.regatta.domain.Regatta;
import be.ucll.ip.minor.groep5610.regatta.domain.RegattaService;
import jakarta.validation.Valid;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class RegattaController {

    private static Logger LOGGER = LoggerFactory.getLogger(RegattaController.class);


    private final RegattaService regattaService;
    boolean sortDirAsc = true;

    @Autowired
    public RegattaController(RegattaService regattaService) {
        this.regattaService = regattaService;
    }

    @GetMapping("/regatta/overview")
    public String overview(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "2") int size , Model model){
        Page<Regatta> regattaPage = regattaService.getRegattaPage(page, size);

        if (regattaPage.isEmpty()) {
            createSampleData();
            regattaPage = regattaService.getRegattaPage(page, size);
        }
        model.addAttribute("searchDto", new RegattaSearchDto());
        model.addAttribute("regattas", regattaPage);
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
        } catch (ServiceException exc) {
            model.addAttribute("error", exc.getMessage());
            return "regatta/add";
        }
    }

    @GetMapping("/regatta/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        Regatta regatta = regattaService.getRegatta(id);
        model.addAttribute("regatta", toDto(regatta));
        return "regatta/delete-confirmation";
    }

    @PostMapping("/regatta/delete/{id}")
    public String deleteConfirm(@PathVariable("id") long id, Model model) {
        try {
            regattaService.deleteRegatta(id);
            return "redirect:/regatta/overview";
        } catch (ServiceException exc){
            model.addAttribute("error", exc.getMessage());
            Regatta regatta = regattaService.getRegatta(id);
            model.addAttribute("regatta", toDto(regatta));
            return "regatta/delete-confirmation";
        }
    }

    @GetMapping("/regatta/update")
    public String update(@RequestParam("id") long id, Model model){
        Regatta regatta = regattaService.getRegatta(id);
        model.addAttribute("regattaDto", toDto(regatta));
        return "regatta/update";
    }

    @PostMapping("/regatta/update")
    public String update(@RequestParam("id") long id, @Valid RegattaDto dto, BindingResult result, Model model) {
        Regatta regatta = regattaService.getRegatta(id);
        try {
            if(result.hasErrors()) {
                model.addAttribute("regatta", toDto(regatta));
                return "regatta/update";
            }
            regattaService.updateRegatta(dto, regatta);
            return "redirect:/regatta/overview";
        } catch (ServiceException exc) {
            model.addAttribute("error", exc.getMessage());
            return "regatta/update";
        }
    }

    @GetMapping("/regatta/searchAndSort")
    public String searchAndSort(@ModelAttribute(value = "searchDto") RegattaSearchDto searchDto,
                                @RequestParam(value = "sort", defaultValue = "id") String sort,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "2") int size,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            if(sortDirAsc) {
                sortDirAsc = false;
                Page<Regatta> regattas = regattaService.searchAndSort(searchDto, sort, "asc", page, size);
                model.addAttribute("regattas", regattas);
            } else {
                sortDirAsc = true;
                Page<Regatta> regattas = regattaService.searchAndSort(searchDto, sort,  "desc", page, size);
                model.addAttribute("regattas", regattas);
            }
            model.addAttribute("searchAndSortUrl", searchAndSortUrl(searchDto, sort));
            return "regatta/overview";
        } catch (ServiceException exc) {
            redirectAttributes.addFlashAttribute("error", exc.getMessage());
            return "redirect:/regatta/overview";
        }
    }

    public RegattaDto toDto(Regatta regatta) {
        RegattaDto regattaDto = new RegattaDto();
        regattaDto.setId(regatta.getId());
        regattaDto.setWedstrijdNaam(regatta.getWedstrijdNaam());
        regattaDto.setName(regatta.getName());
        regattaDto.setDate(regatta.getDate());
        regattaDto.setMaxTeams(regatta.getMaxTeams());
        regattaDto.setCategorie(regatta.getCategorie());
        return regattaDto;
    }

    private void createSampleData() {
        RegattaDto regatta1 = new RegattaDto();
        regatta1.setWedstrijdNaam("wedstrijd1");
        regatta1.setName("d_club1");
        regatta1.setDate(LocalDate.now());
        regatta1.setMaxTeams(5);
        regatta1.setCategorie("categorie1");

        RegattaDto regatta2 = new RegattaDto();
        regatta2.setWedstrijdNaam("wedstrijd2");
        regatta2.setName("a_club2");
        regatta2.setDate(LocalDate.now().plusDays(5));
        regatta2.setMaxTeams(3);
        regatta2.setCategorie("categorie2");

        RegattaDto regatta3 = new RegattaDto();
        regatta3.setWedstrijdNaam("wedstrijd2");
        regatta3.setName("g_club2");
        regatta3.setDate(LocalDate.now().plusDays(1));
        regatta3.setMaxTeams(3);
        regatta3.setCategorie("categorie2");

        RegattaDto regatta4 = new RegattaDto();
        regatta4.setWedstrijdNaam("wedstrijd4");
        regatta4.setName("z_club1");
        regatta4.setDate(LocalDate.now());
        regatta4.setMaxTeams(5);
        regatta4.setCategorie("categorie1");

        RegattaDto regatta5 = new RegattaDto();
        regatta5.setWedstrijdNaam("wedstrijd5");
        regatta5.setName("e_club2");
        regatta5.setDate(LocalDate.now().plusDays(5));
        regatta5.setMaxTeams(3);
        regatta5.setCategorie("categorie2");

        RegattaDto regatta6 = new RegattaDto();
        regatta6.setWedstrijdNaam("wedstrijd6");
        regatta6.setName("y_club2");
        regatta6.setDate(LocalDate.now().plusDays(1));
        regatta6.setMaxTeams(3);
        regatta6.setCategorie("categorie2");

        regattaService.createRegatta(regatta1);
        regattaService.createRegatta(regatta2);
        regattaService.createRegatta(regatta3);
        regattaService.createRegatta(regatta4);
        regattaService.createRegatta(regatta5);
        regattaService.createRegatta(regatta6);
    }

    private String searchAndSortUrl(RegattaSearchDto searchDto, String sort) {
        return "/regatta/searchAndSort?"
                + "sort=" + sort
                + "&dateAfter=" + searchDto.getDateAfter()
                + "&dateBefore=" + searchDto.getDateBefore()
                + "&category=" + searchDto.getCategory();
    }
}
