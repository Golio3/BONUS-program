package bonus.web.controller;

import bonus.web.model.BonusModel;
import bonus.web.model.UserBonusModel;
import bonus.web.model.UsersModel;
import bonus.web.repository.BonusRepository;
import bonus.web.repository.UserBonusRepository;
import bonus.web.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/manage")
public class UserController {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    BonusRepository bonusRepository;

    @Autowired
    UserBonusRepository userBonusRepository;

    @GetMapping("/user")
    public String userForm(Model model, Authentication authentication) {

        String username = authentication.getName();
        UsersModel user = usersRepository.findByUsername(username);
        model.addAttribute("user", user);

        List<BonusModel> bonuses = bonusRepository.findAll();
        bonuses.forEach(folder -> {
            if (folder.getImage() != null) {
                String img = Base64.getEncoder().encodeToString(folder.getImage());
                folder.setImageStr(img);
            }
        });
        model.addAttribute("bonuses", bonuses);

        List<UserBonusModel> userBonus = userBonusRepository.findAllByUserIdAndStatus(user.getId(), 1);
        Integer costAll = 0, numberAll = 0;
        for (UserBonusModel ub : userBonus) {
            costAll = costAll + ub.getBonus().getCost() * ub.getNumber();
            numberAll = numberAll + ub.getNumber();
        }

        model.addAttribute("numberAll", numberAll);
        model.addAttribute("costAll", costAll);

        return "admin/manage/user/user";
    }

    @GetMapping("/user/{id}/add/{bonusId}")
    public String addBonus(@PathVariable Integer id, @PathVariable Integer bonusId) {
        UsersModel user = usersRepository.findOne(id);
        BonusModel bonus = bonusRepository.findOne(bonusId);

        UserBonusModel userBonus = userBonusRepository.findByUserIdAndBonusIdAndStatus(id, bonusId, 1);
        if (userBonus == null) {
            userBonus = new UserBonusModel(1, 1, user, bonus);
        } else {
            userBonus.setNumber(userBonus.getNumber() + 1);
        }

        userBonusRepository.save(userBonus);
        user.setPointsActual(user.getPointsActual() - bonus.getCost());
        usersRepository.save(user);
        return "redirect:/manage/user";
    }

    @GetMapping("/user/bonus")
    public String listBonus(Model model, Authentication authentication) {
        String username = authentication.getName();
        UsersModel user = usersRepository.findByUsername(username);
        model.addAttribute("user", user);

        List<UserBonusModel> userBonus = userBonusRepository.findAllByUserIdAndStatus(user.getId(), 1);
        Integer costAll = 0;
        for (UserBonusModel ub : userBonus) {
            costAll = costAll + ub.getBonus().getCost() * ub.getNumber();
        }
        model.addAttribute("userBonus", userBonus);
        model.addAttribute("costAll", costAll);

        return "admin/manage/user/order";
    }

    @GetMapping("/user/bonus/minus/{id}")
    public String minusBonus(@PathVariable Integer id) {
        UserBonusModel userBonus = userBonusRepository.findOne(id);
        UsersModel user = userBonus.getUser();
        user.setPointsActual(user.getPointsActual() + userBonus.getBonus().getCost());
        if (userBonus.getNumber() > 1) {
            userBonus.setNumber(userBonus.getNumber() - 1);
            userBonusRepository.save(userBonus);
        } else {
            userBonusRepository.delete(userBonus);
        }
        return "redirect:/manage/user/bonus";
    }

    @GetMapping("/user/bonus/plus/{id}")
    public String plusBonus(@PathVariable Integer id) {
        UserBonusModel userBonus = userBonusRepository.findOne(id);
        UsersModel user = userBonus.getUser();
        if (user.getPointsActual() >= userBonus.getBonus().getCost()) {
            user.setPointsActual(user.getPointsActual() - userBonus.getBonus().getCost());
            usersRepository.save(user);
            userBonus.setNumber(userBonus.getNumber() + 1);
            userBonusRepository.save(userBonus);
        }
        return "redirect:/manage/user/bonus";
    }

    @GetMapping("/user/bonus/remove/{id}")
    public String removeBonus(@PathVariable Integer id) {
        UserBonusModel userBonus = userBonusRepository.findOne(id);
        UsersModel user = userBonus.getUser();
        user.setPointsActual(user.getPointsActual() + userBonus.getBonus().getCost());
        userBonusRepository.delete(userBonus);
        return "redirect:/manage/user/bonus";
    }

    @GetMapping("/user/bonus/confirm")
    public String confirmBonus(Authentication authentication) {
        String username = authentication.getName();
        UsersModel user = usersRepository.findByUsername(username);
        List<UserBonusModel> userBonus = userBonusRepository.findAllByUserIdAndStatus(user.getId(), 1);
        for (UserBonusModel ub : userBonus) {
            UserBonusModel oldUb = userBonusRepository.findByUserIdAndBonusIdAndStatus(ub.getUser().getId(), ub.getBonus().getId(), 2);
            if (oldUb != null) {
                oldUb.setNumber(oldUb.getNumber() + ub.getNumber());
                userBonusRepository.save(oldUb);
                userBonusRepository.delete(ub);
            } else {
                ub.setStatus(2);
                userBonusRepository.save(ub);
            }
        }
        user.setPoints(user.getPointsActual());
        usersRepository.save(user);

        return "redirect:/manage/user";
    }
}
