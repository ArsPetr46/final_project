package sumdu.edu.ua.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import sumdu.edu.ua.database.DatabaseConnector;

@Controller
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private DatabaseConnector database;

    @RequestMapping("/update/{orderId}/{newStatus}")
    public RedirectView updateOrderStatus(@PathVariable("orderId") Integer orderId, @PathVariable("newStatus") String newStatus) {
        database.updateOrderStatus(orderId, newStatus);
        return new RedirectView("/marketplace/main_menu/all_orders");
    }
}
