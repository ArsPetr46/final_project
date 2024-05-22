package sumdu.edu.ua.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import sumdu.edu.ua.database.DatabaseConnector;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/store")
public class StoreController {
    @Autowired
    DatabaseConnector database;

    @PostMapping(value = "/{action}/{productId}")
    public Object store(@PathVariable("action") String action,
                        @PathVariable(required = false) Integer productId,
                        @RequestParam(required = false) Integer quantity,
                        HttpSession session) {

        if (!database.productExists(productId)) {
            return new RedirectView("/marketplace/main_menu/store?error=Product does not exist");
        }

        switch (action) {

            case "order" -> {
                if (quantity > database.getCurrentQuantity(productId)) {
                    return new RedirectView("/marketplace/main_menu/store?error=Not enough quantity in stock");
                }

                ModelAndView modelAndView = new ModelAndView("make_order");
                modelAndView.addObject("product", database.getProductById(productId));
                modelAndView.addObject("quantity", quantity);
                return modelAndView;
            }
            case "add_to_wishlist" -> database.addToWishlist(productId, session.getAttribute("username").toString());
            case "remove_from_wishlist" -> database.removeFromWishlist(productId, session.getAttribute("username").toString());
            case "edit" -> {
                if (!session.getAttribute("role").equals("admin")) {
                    return new RedirectView("/marketplace/main_menu/store");
                }
                ModelAndView modelAndView = new ModelAndView("modify_products");
                modelAndView.addObject("product", database.getProductById(productId));
                modelAndView.addObject("types", database.getAllTypes());
                return modelAndView;
            }
            case "delete" -> {
                if (!session.getAttribute("role").equals("admin")) {
                    return new RedirectView("/marketplace/main_menu/store");
                }
                if (!database.deleteProduct(productId)) {
                    return new RedirectView("/marketplace/main_menu/store?error=Error during deleting product");
                }
            }
        }

        return new RedirectView("/marketplace/main_menu/store");
    }

    @PostMapping(value = "/{action}")
    public Object store(@PathVariable("action") String action,
                              @RequestParam(required = false) Integer productId,
                              @RequestParam(required = false) Double price,
                              @RequestParam(required = false) Integer quantity,
                              @RequestParam(required = false) String email,
                              @RequestParam(required = false) String payment,
                              @RequestParam(required = false) String delivery,
                              @RequestParam(required = false) String adminAction,
                              @RequestParam(required = false) String productName,
                              @RequestParam(required = false) String productDescription,
                              @RequestParam(required = false) Integer productTypeId,
                              @RequestParam(required = false) Double productPrice,
                              @RequestParam(required = false) Integer productQuantity,
                              HttpSession session) {

        if (quantity != null && quantity > database.getCurrentQuantity(productId)) {
            return new RedirectView("/marketplace/main_menu/store?error=Not enough quantity in stock");
        }

        switch (action) {
            case "process_order" -> {
                database.insertOrder(session.getAttribute("username").toString(), productId, price, quantity, email, payment, delivery);
                return new RedirectView("/marketplace/main_menu/store");
            }
            case "add" -> {
                if (!session.getAttribute("role").equals("admin")) {
                    return new RedirectView("/marketplace/main_menu/store");
                }
                switch (adminAction) {
                    case "Add Product" -> {
                        return new ModelAndView("add_product", "types", database.getAllTypes());
                    }
                }
            }
            case "process_add" -> {
                if (!session.getAttribute("role").equals("admin")) {
                    return new RedirectView("/marketplace/main_menu/store");
                }

                if(database.insertProduct(productName, productDescription, productTypeId, productPrice, productQuantity)) {
                    return new RedirectView("/marketplace/main_menu/store");
                } else {
                    return new RedirectView("/marketplace/main_menu/store?error=Product name already exists");
                }
            }
            case "process_edit" -> {
                if (!session.getAttribute("role").equals("admin")) {
                    return new RedirectView("/marketplace/main_menu/store");
                }

                if (!database.updateProduct(productId, productName, productDescription, productTypeId, productPrice, productQuantity)) {
                    return new RedirectView("/marketplace/main_menu/store?error=Error during updating product");
                }
            }
        }

        return new RedirectView("/marketplace/main_menu/store");
    }

    @GetMapping(value = "/product/{productId}")
    public ModelAndView product(@PathVariable("productId") Integer productId) {
        return new ModelAndView("product", "product", database.getProductById(productId));
    }

    @GetMapping(value = "/check_product_name/{productName}")
    @ResponseBody
    public Map<String, Boolean> checkProductName(@PathVariable("productName") String productName) {
        boolean exists = database.checkProductName(productName);
        Map<String, Boolean> map = new HashMap<>();
        map.put("exists", exists);
        return map;
    }
}
