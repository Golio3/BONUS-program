package bonus.web.controller;

import bonus.web.model.ContactModel;
import bonus.web.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manage")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/contact")
    public String contact(Model model) {
        ContactModel contact = contactRepository.findOne(1);
        model.addAttribute("contact", contact);
        return "admin/manage/contact/contact";
    }

    @PostMapping("/contact")
    public String save(@ModelAttribute("contact") ContactModel contact, Model model) {
        contact.setId(1);
        contactRepository.save(contact);

        model.addAttribute("contact", contact);
        return "redirect:/manage/contact";
    }
}
