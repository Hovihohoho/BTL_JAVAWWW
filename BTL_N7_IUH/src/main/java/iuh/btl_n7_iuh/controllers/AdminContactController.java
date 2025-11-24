package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminContactController {
    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/admin/contacts")
    public String listContacts(Model model) {
        model.addAttribute("contacts", contactRepository.findAll());
        return "admin/contacts";
    }
}

