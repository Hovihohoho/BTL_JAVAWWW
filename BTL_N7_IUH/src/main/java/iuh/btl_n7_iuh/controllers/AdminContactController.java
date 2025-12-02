package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminContactController {
    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/admin/contacts")
    public String listContacts(Model model) {
        model.addAttribute("contacts", contactRepository.findAll());
        return "admin/contacts";
    }

    @PostMapping("/admin/contacts/{id}/delete")
    public String deleteContact(@PathVariable Long id) {
        contactRepository.deleteById(id);
        return "redirect:/admin/contacts";
    }
}
