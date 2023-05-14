package be.ucll.ip.minor.groep5610;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private static Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/home")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
