package bonus.web.controller;

import bonus.storage.FileStorageException;
import bonus.web.controller.page.FoldersModel;
import bonus.web.controller.page.HomeModel;
import bonus.web.repository.FoldersRepository;
import bonus.web.repository.HomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
@SessionAttributes("home")
@RequestMapping("/manage")
public class HomePageController {

    @Autowired
    private HomeRepository homeRepository;

    @Autowired
    private FoldersRepository foldersRepository;

    @GetMapping("/home")
    public String home(Model model) {
        HomeModel home = homeRepository.findOne(1);
        List<FoldersModel> folders = foldersRepository.findAll();

        model.addAttribute("home", home);
        model.addAttribute("folders", folders);
        return "admin/manage/home/home";
    }

    @PostMapping(path = {"/home"})
    public String save(@ModelAttribute("home") HomeModel home, Model model) {
        homeRepository.save(home);
        return "redirect:/manage/home";
    }

    @PostMapping(path = {"/home/rem-folder"})
    public String remFolders(@ModelAttribute("home") HomeModel home, @ModelAttribute("remIndex") int remIndex, Model model) {
//        page.getToggles().add(new TogglesModel());
//        page.setText(page.getText() + " qq ");
//        home.getFolders().remove(remIndex);
        model.addAttribute("home", home);
        return "redirect:/manage/home";
    }

    @PostMapping(path = {"/home/add-folder"})
    public String addFolders(@ModelAttribute("home") HomeModel home, @ModelAttribute("title") String title, @ModelAttribute("content") String content,
                             @ModelAttribute("foldersId") String foldersId, @RequestParam("foldersImage") MultipartFile foldersImage, Model model) {
        FoldersModel foldersModel = new FoldersModel();
        try {
            foldersModel = foldersRepository.findOne(Integer.decode(foldersId));
        } catch (Exception e) {

        }
        foldersModel.setTitle(title);
        foldersModel.setContent(content);
        if (!foldersImage.isEmpty()) {
            foldersModel.setImage(storeFile(foldersImage));
        }
        foldersRepository.save(foldersModel);
        model.addAttribute("home", home);
        return "redirect:/manage/home";
    }

    @PostMapping(path = {"/home/edit-folder"})
    public FoldersModel editFolders(@ModelAttribute("index") int index, Model model) {
        FoldersModel fm = foldersRepository.findOne(index);
        if (fm.getImage() != null) {
            String img = Base64.getEncoder().encodeToString(fm.getImage());
            model.addAttribute("imgVis", img);
        }
        return fm;
    }

    private byte[] storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            return file.getBytes();
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
