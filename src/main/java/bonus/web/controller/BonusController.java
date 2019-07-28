package bonus.web.controller;

import bonus.web.FileStorageException;
import bonus.web.model.BonusModel;
import bonus.web.repository.BonusRepository;
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
@RequestMapping("/manage")
public class BonusController {

    @Autowired
    BonusRepository bonusRepository;

    @GetMapping("/admin/bonus")
    public String bonusForm(Model model) {
        List<BonusModel> bonuses = bonusRepository.findAll();
        model.addAttribute("bonuses", bonuses);
        return "admin/manage/admin/bonus";
    }

    @GetMapping("/admin/bonus/add")
    public String addBonus(Model model) {
        BonusModel bonus = new BonusModel();
        model.addAttribute("bonus", bonus);
        return "admin/manage/admin/editBonus";
    }

    @GetMapping("/admin/bonus/edit/{id}")
    public String editBonus(@PathVariable Integer id, Model model) {
        BonusModel bonus = bonusRepository.findOne(id);
        if (bonus.getImage() != null) {
            String img = Base64.getEncoder().encodeToString(bonus.getImage());
            model.addAttribute("imgVis", img);
        }

        model.addAttribute("bonus", bonus);
        return "admin/manage/admin/editBonus";
    }

    @PostMapping("/admin/bonus/save")
    public String bonusSave(@ModelAttribute("bonus") BonusModel bonus,
                            @RequestParam("imageFile") MultipartFile imageFile, Model model) {
        if (!imageFile.isEmpty()) {
            byte[] image = storeFile(imageFile);
            String img = Base64.getEncoder().encodeToString(image);
            model.addAttribute("imgVis", img);
            bonus.setImage(image);
        }
        bonusRepository.save(bonus);
        return "redirect:/manage/admin/bonus";
    }

    @GetMapping("/admin/bonus/delete/{id}")
    public String deleleBonus(@PathVariable Integer id) {
        bonusRepository.delete(id);
        return "redirect:/manage/admin/bonus";
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
