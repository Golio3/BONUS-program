package bonus.web.controller;

import bonus.web.model.UsersModel;
import bonus.web.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/manage")
public class AdminController {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UsersModel timeUser;

    @GetMapping("/admin")
    public String adminForm(Model model) {

//        UsersModel user = usersRepository.findOne(3);
//        model.addAttribute("user", user);

        List<UsersModel> users = usersRepository.findByRoles("USER");
        model.addAttribute("users", users);
        return "admin/manage/admin/admin";
    }

    @GetMapping("/admin/add")
    public String addUser(Model model) {
        UsersModel user = new UsersModel();
        model.addAttribute("user", user);
        return "admin/manage/admin/editUser";
    }

    @GetMapping("/admin/edit")
    public String edit(Model model) {
        if (timeUser != null) {
            model.addAttribute("user", timeUser);
        } else {
            return "redirect:/manage/admin";
        }
        return "admin/manage/admin/editUser";
    }

    @GetMapping("/admin/edit/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        UsersModel user = usersRepository.findOne(id);
        user.setPassword("");
        model.addAttribute("user", user);
        return "admin/manage/admin/editUser";
    }

    @PostMapping("/admin/save")
    public String addUserSave(@ModelAttribute("user") UsersModel user, Model model) {
        user = saveUser(user);
        if (user.getErrors() != null && user.getErrors().length() > 0) {
            timeUser = user;
            return "redirect:/manage/admin/edit";
        } else {
            if (user.getId() != null) {
                UsersModel oldUser = usersRepository.findOne(user.getId());
                if (user.getPassword().contentEquals("")) {
                    user.setPassword(oldUser.getPassword());
                }
                user.setDateStart(oldUser.getDateStart());
                user.setNextPossibleSignature(oldUser.getNextPossibleSignature());
                user.setLastSignature(oldUser.getLastSignature());
                user.setPointsActual(user.getPoints() - (oldUser.getPoints() - oldUser.getPointsActual()));
            } else {
                user.setActive(1);
                user.setDateStart(new Date());
                user.setRoles("USER");
                user.setPointsActual(user.getPoints());
            }

            usersRepository.save(user);
            return "redirect:/manage/admin";
        }
    }

    private UsersModel saveUser(UsersModel user) {
        user.setErrors("");
        if (user.getUsername().length() <= 3) {
            user.setErrors("\r\n Chyba - uživatelské jméno nesmí být menší než 4 znaky");
        }
        if ((!user.getPassword().contentEquals("") || user.getId() == 0) && user.getPassword().length() <= 3) {
            user.setErrors("\r\n Chyba - uživatelské heslo nesmí být menší než 4 znaky");
        }
        if (user.getUsername().length() > 20 || user.getPassword().length() > 20) {
            user.setErrors("\r\n Chyba - uživatelské jméno a heslo nesmí být více než 20 znaků");
        }
        UsersModel testUser = usersRepository.findByUsername(user.getUsername());
        if (testUser != null && (user.getId() == null || !user.getId().equals(testUser.getId()))) {
            user.setErrors("\r\n Chyba - uživatel se stejným uživatelským jménem je již zaregistrován");
        }


        if (user.getErrors().length() == 0 && !user.getPassword().contentEquals("")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }


        return user;
    }

    @GetMapping("/admin/delete/{id}")
    public String deleleUser(@PathVariable Integer id) {
        usersRepository.delete(id);
        return "redirect:/manage/admin";
    }
}
