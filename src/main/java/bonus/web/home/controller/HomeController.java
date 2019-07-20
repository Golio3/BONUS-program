package bonus.web.home.controller;

import bonus.web.controller.page.FoldersModel;
import bonus.web.controller.page.HomeModel;
import bonus.web.controller.page.PageTreeModel;
import bonus.web.repository.FoldersRepository;
import bonus.web.repository.HomeRepository;
import bonus.web.repository.PageTreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    HomeRepository homeRepository;

    @Autowired
    FoldersRepository foldersRepository;

    @Autowired
    PageTreeRepository pageTreeRepository;

    @GetMapping()
    public String home(Model model) {

        HomeModel home = homeRepository.findOne(1);
        List<FoldersModel> folders = foldersRepository.findAll();

        model.addAttribute("home", home);

        String headContext = parseStr(home.getHeadContext());
        String bodyContext = parseStr(home.getBodyContext());
        String downContext = parseStr(home.getDownContext());
        model.addAttribute("headContext", headContext);
        model.addAttribute("bodyContext", bodyContext);
        model.addAttribute("downContext", downContext);

        folders.forEach(folder -> {
            if (folder.getImage() != null) {
                String img = Base64.getEncoder().encodeToString(folder.getImage());
                folder.setImageStr(img);
            }
        });
        model.addAttribute("folders", folders);

        List<PageTreeModel> pageTree = pageTreeRepository.findAll();
        model.addAttribute("pageTree", pageTree);

        return "index";
    }

    private String parseStr(String str) {
        str = str.replaceAll("&lt;", "<");
        str = str.replaceAll("&gt;", ">");
        return str;
    }
}
