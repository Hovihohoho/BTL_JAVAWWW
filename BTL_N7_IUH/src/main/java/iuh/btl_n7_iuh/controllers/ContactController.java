package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.entities.Contact;
import iuh.btl_n7_iuh.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {
    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/contact")
    public String showContactForm() {
        return "contact";
    }

    @PostMapping("/contact")
    public String submitContact(@RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String message,
                                Model model) {
        try {
            Contact contact = new Contact(name, email, message);
            contactRepository.save(contact);
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("success", false);
        }
        return "contact";
    }
}
