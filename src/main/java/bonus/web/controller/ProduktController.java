package bonus.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manage")
public class ProduktController {

    @GetMapping("/produkt")
    public String produkt() {
        return "admin/manage/produkt/produkt";
    }
}
