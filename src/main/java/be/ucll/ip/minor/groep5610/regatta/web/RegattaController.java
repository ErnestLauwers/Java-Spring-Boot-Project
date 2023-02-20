package be.ucll.ip.minor.groep5610.regatta.web;

import be.ucll.ip.minor.groep5610.regatta.domain.Regatta;
import be.ucll.ip.minor.groep5610.regatta.domain.RegattaService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegattaController {

    private static Logger LOGGER = LoggerFactory.getLogger(RegattaController.class);


    private final RegattaService regattaService;

    @Autowired
    public RegattaController(RegattaService regattaService) {
        this.regattaService = regattaService;
    }

    @GetMapping("/regatta")
    public String index(){
        System.out.println("index");
        return "regatta/index.html";
    }



}
