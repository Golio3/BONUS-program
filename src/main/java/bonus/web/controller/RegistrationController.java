package bonus.web.controller;

import bonus.web.model.UsersModel;
import bonus.web.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping()
    public String formRegistration(Model model) {
        UsersModel reg = new UsersModel();
        model.addAttribute("reg", reg);
        return "admin/manage/registration/registration";
    }

    @PostMapping()
    public String register(@ModelAttribute("reg") UsersModel reg, Model model) {

        if (reg.getUsername().length() <= 3 || reg.getPassword().length() <= 3 || reg.getRepeatPassword().length() <= 3) {
            reg.setErrors("Chyba - uživatelské jméno a heslo nesmí být menší než 4 znaky");
            model.addAttribute("reg", reg);
            return "admin/manage/registration/registration";
        }

        if (reg.getUsername().length() > 20 || reg.getPassword().length() > 20 || reg.getRepeatPassword().length() > 20) {
            reg.setErrors("Chyba - uživatelské jméno a heslo nesmí být více než 20 znaků");
            model.addAttribute("reg", reg);
            return "admin/manage/registration/registration";
        }

        if (!reg.getPassword().contentEquals(reg.getRepeatPassword())) {
            reg.setErrors("Chyba - hesla musí odpovídat");
            model.addAttribute("reg", reg);
            return "admin/manage/registration/registration";
        }

        UsersModel testUser = usersRepository.findByUsername(reg.getUsername());
        if (testUser != null) {
            reg.setErrors("Chyba - uživatel se stejným uživatelským jménem je již zaregistrován");
            model.addAttribute("reg", reg);
            return "admin/manage/registration/registration";
        }

        reg.setPassword(passwordEncoder.encode(reg.getPassword()));
        reg.setRoles("USER");
        reg.setActive(1);

        reg.setDateStart(new Date());
        usersRepository.save(reg);

        return "redirect:/manage/login";
    }
}
