package bonus.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    AuthenticationManagerBuilder auth;

    @Autowired
    AuthenticationProvider authenticationProvider;

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.toArray()[0].toString();

        if (role.contentEquals("ROLE_ADMIN")) {
            return "redirect:/manage/admin";
        } else {
            return "redirect:/manage/user";
        }
    }
}
