package sumdu.edu.ua.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import sumdu.edu.ua.database.DatabaseConnector;
import sumdu.edu.ua.models.User;

import javax.xml.crypto.Data;

@Controller
public class AuthorizationController {

    @Autowired
    DatabaseConnector database;

    @GetMapping(value = "/")
    public String index() {
        return "authorization";
    }


    @PostMapping(value = "/auth")
    public RedirectView login(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              @RequestParam("action") String action,
                              HttpSession session) {

        RedirectView redirectView = new RedirectView();

        switch (action.toLowerCase()) {
            case "login":
                if (database.checkUser(username, password)) {
                    session.setAttribute("username", username);
                    session.setAttribute("role", database.getUserRole(username));
                    redirectView.setUrl("/marketplace/main_menu");
                    return redirectView;
                }
                redirectView.setUrl("/marketplace");
                redirectView.addStaticAttribute("error", "Invalid username or password");
                return redirectView;

            case "signup":
                if (database.insertUser(username, password)) {
                    session.setAttribute("username", username);
                    session.setAttribute("role", "user");
                    redirectView.setUrl("/marketplace/main_menu");
                    return redirectView;
                }
                redirectView.setUrl("/marketplace");
                redirectView.addStaticAttribute("error", "User already exists");
                return redirectView;

            default:
                redirectView.setUrl("/marketplace");
                redirectView.addStaticAttribute("error", "Invalid action");
                return redirectView;
        }
    }

    @GetMapping(value = "/logout")
    public RedirectView logout(HttpSession session) {
        session.removeAttribute("username");
        session.removeAttribute("role");
        return new RedirectView("/marketplace");
    }
}
