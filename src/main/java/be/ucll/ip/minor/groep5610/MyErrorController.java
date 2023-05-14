package be.ucll.ip.minor.groep5610;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = null;

        switch(status.toString()) {
            case "401":
                message = "You are not authorized (" + status + ")";
                break;
            case "403":
                message = "This is forbidden (" + status + ")";
                break;
            case "404":
                message = "What you are looking for could not be found (" + status + ")";
                break;
            case "500":
                message = "There seems to be a problem with the server (" + status + ")";
                break;
            case "502":
                message = "Something is wrong with the gateway (" + status + ")";
                break;
            case "503":
                message = "Services are unavailable (" + status + ")";
                break;
            default:
                message = HttpStatus.resolve((Integer) status).toString();
                break;
        }
        model.addAttribute("error", message);
        return "error";
    }
}
