package bonus.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manage")
public class LoginController {

    @GetMapping("/")
    public String root() {
        return "/index";
    }

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/admin/error/access-denied";
    }

}
