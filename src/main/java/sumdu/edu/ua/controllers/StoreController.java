package sumdu.edu.ua.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.RedirectView;
import sumdu.edu.ua.database.DatabaseConnector;

@Controller
public class StoreController {
    @Autowired
    DatabaseConnector database;

    @PostMapping(value = "/store/{action}/{productId}")
    public Object store(@PathVariable("action") String action,
                                      @PathVariable(required = false) Integer productId,
                                      @RequestParam(required = false) Integer quantity,
                                      HttpSession session) {

        switch (action) {
            case "order" -> {
                if (quantity > database.getCurrentQuantity(productId)) {
                    return new RedirectView("/marketplace/main_menu/store?error=Not enough quantity in stock");
                }

                ModelAndView modelAndView = new ModelAndView("order");
                modelAndView.addObject("product", database.getProductById(productId));
                modelAndView.addObject("quantity", quantity);
                return modelAndView;
            }
            case "add_to_wishlist" -> database.addToWishlist(productId, session.getAttribute("username").toString());
            case "remove_from_wishlist" -> database.removeFromWishlist(productId, session.getAttribute("username").toString());
        }

        return new RedirectView("/marketplace/main_menu/store");
    }

    @PostMapping(value = "/store/{action}")
    public RedirectView store(@PathVariable("action") String action,
                              @RequestParam(required = false) Integer productId,
                              @RequestParam(required = false) Double price,
                              @RequestParam(required = false) Integer quantity,
                              @RequestParam(required = false) String email,
                              @RequestParam(required = false) String payment,
                              @RequestParam(required = false) String delivery,
                              HttpSession session) {

        if (quantity > database.getCurrentQuantity(productId)) {
            return new RedirectView("/marketplace/main_menu/store?error=Not enough quantity in stock");
        }

        switch (action) {
            case "process_order" -> {
                database.insertOrder(session.getAttribute("username").toString(), productId, price, quantity, email, payment, delivery);
                return new RedirectView("/marketplace/main_menu/store");
            }
        }

        return new RedirectView("/marketplace/main_menu/store");
    }
}
