package sumdu.edu.ua.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sumdu.edu.ua.database.DatabaseConnector;

@Controller
public class MainMenuController {
    @Autowired
    private DatabaseConnector database;

    @GetMapping(value = "/main_menu")
    private String mainMenu() {
        return "template";
    }

    @GetMapping(value = "/main_menu/{action}")
    public ModelAndView mainMenu(@PathVariable("action") String action,
                                 @RequestParam(required = false, defaultValue = "") String name,
                                 @RequestParam(required = false, defaultValue = "All") String filter,
                                 @RequestParam(required = false, defaultValue = "No sorting") String sort,
                                 @RequestParam(required = false, defaultValue = "ASC") String order,
                                 @RequestParam(required = false, defaultValue = "false") Boolean wishlisted,
                                 HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("template");


        switch (action) {
            case "store" -> {
                modelAndView.addObject("action", action);
                modelAndView.addObject("types", database.getTypeNames());
                modelAndView.addObject("products", database.getProductsForUser(session.getAttribute("username").toString(), name, filter, sort, order, wishlisted));
                modelAndView.addObject("role", session.getAttribute("role"));
            }
            case "types" -> {
                modelAndView.addObject("action", action);
                modelAndView.addObject("types", database.getAllTypes());
                modelAndView.addObject("role", session.getAttribute("role"));
            }
            case "user_orders" -> {
                modelAndView.addObject("action", "orders");
                modelAndView.addObject("orders", database.getOrdersForUser(session.getAttribute("username").toString()));
            }
            case "all_orders" -> {
                modelAndView.addObject("action", "orders");
                modelAndView.addObject("orders", database.getAllOrders());
            }
        }

        return modelAndView;
    }

}
