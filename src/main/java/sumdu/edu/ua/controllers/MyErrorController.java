package sumdu.edu.ua.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyErrorController implements ErrorController {
    @RequestMapping("/error")
    public ModelAndView error(HttpServletRequest request) {
        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        return new ModelAndView("error", "exception", throwable);
    }
}
