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
@RequestMapping("/types")
public class TypesController {
    @Autowired
    private DatabaseConnector database;

    @PostMapping(value = "/{action}/{typeId}")
    public Object typesActionOnId(@PathVariable String action,
                        @PathVariable Integer typeId,
                        HttpSession session) {

        if (!session.getAttribute("role").equals("admin")) {
            return new RedirectView("/marketplace/main_menu/types");
        }

        switch (action) {
            case "edit" -> {
                return new ModelAndView("modify_types", "type", database.getTypeById(typeId));
            }
            case "delete" -> {
                if (!database.deleteType(typeId)) {
                    return new RedirectView("/marketplace/main_menu/types?error=Error during deleting type");
                }
            }
        }

        return new RedirectView("/marketplace/main_menu/types");
    }

    @PostMapping(value = "/{action}")
    public Object typesAction(@PathVariable("action") String action,
                              @RequestParam(required = false) Integer typeId,
                              @RequestParam(required = false) String typeName,
                              @RequestParam(required = false) String typeDescription) {
        switch (action) {
            case "add" -> {
                return new ModelAndView("modify_types");
            }
            case "process_add" -> {
                if (!database.insertType(typeName, typeDescription)) {
                    return new RedirectView("/marketplace/main_menu/types?error=Error during adding type");
                }
            }
            case "process_edit" -> {
                if (!database.updateType(typeId, typeName, typeDescription)) {
                    return new RedirectView("/marketplace/main_menu/types?error=Error during updating type");
                }
            }
        }

        return new RedirectView("/marketplace/main_menu/types");
    }
}
