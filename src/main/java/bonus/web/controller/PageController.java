package bonus.web.controller;

import bonus.storage.FileStorageException;
import bonus.web.LoggingAccessDeniedHandler;
import bonus.web.controller.page.FoldersModel;
import bonus.web.controller.page.PageModel;
import bonus.web.controller.page.PageTreeModel;
import bonus.web.controller.page.TogglesModel;
import bonus.web.repository.FoldersRepository;
import bonus.web.repository.PageRepository;
import bonus.web.repository.PageTreeRepository;
import bonus.web.repository.TogglesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@SessionAttributes("page")
@RequestMapping("/manage")
public class PageController {

    @Autowired
    private PageTreeRepository pageTreeRepository;

    @Autowired
    private TogglesRepository togglesRepository;

    @Autowired
    private FoldersRepository foldersRepository;

    @Autowired
    private PageRepository pageRepository;

    private static Logger log = LoggerFactory.getLogger(LoggingAccessDeniedHandler.class);

    private List<PageTreeModel> ptmList = new ArrayList<>();

    PageTreeModel pageTreeAdded = new PageTreeModel();

    public PageController() {
    }

    @GetMapping("/page")
    public String page(Model model) {
        ptmList = pageTreeRepository.findAllByOrderByPosition();
        ptmList.forEach(ptm -> {
            if (ptm.getTreeId() != null && !ptm.getTreeId().contentEquals("")) {
                ptm.setTreeId("");
                pageTreeRepository.save(ptm);
            }
        });
        ptmList = pageTreeRepository.findAllByOrderByPosition();
        model.addAttribute("pages", ptmList);
        return "admin/manage/page/page";
    }

    @PostMapping("/page/create-node")
    @ResponseBody
    public String createNode(@RequestParam String id, @RequestParam String text, @RequestParam String parentId,
                             @RequestParam String type, @RequestParam int position, Model model) {
        int typeI = 0;
        if (type.contentEquals("file")) {
            typeI = 1;
        }
        PageTreeModel pageTree = new PageTreeModel();
        pageTree.setTreeId(id);
        pageTree.setText(text);
        pageTree.setType(typeI);
        try {
            pageTree.setParentId(Integer.decode(parentId));
        } catch (Exception e) {
            pageTree.setParentId(pageTreeRepository.findFirstByTreeId(parentId).getId());
        }
        pageTree.setPosition(position);
        pageTreeRepository.save(pageTree);
        addFolders(pageTree);
        return "redirect:/manage/page";
    }

    @PostMapping("/page/rename-node")
    @ResponseBody
    public String renameNode(@RequestParam String id, @RequestParam String text, Model model) {
        PageTreeModel pageTree;
        try {
            Integer intID = Integer.decode(id);
            pageTree = pageTreeRepository.findOne(intID);
        } catch (Exception e) {
            pageTree = pageTreeRepository.findFirstByTreeId(id);
        }
        if (pageTree != null) {
            pageTree.setText(text);
            pageTreeRepository.save(pageTree);
        }
        if (pageTree.getType() == 1 && pageRepository.findOne(pageTree.getId()) == null) {
            PageModel page = new PageModel();
            page.setId(pageTree.getId());
            page.setTitle(pageTree.getText());
            pageRepository.save(page);
        }
        editFolders(pageTree);
        return "redirect:/manage/page";
    }

    @PostMapping("/page/delete-node")
    @ResponseBody
    public String deleteNode(@RequestParam String id, Model model) {
        PageTreeModel pageTree;
        try {
            Integer intID = Integer.decode(id);
            pageTree = pageTreeRepository.findOne(intID);
            onDeletePage(pageTree);
            pageTreeRepository.delete(pageTree);
        } catch (Exception e) {
            pageTree = pageTreeRepository.findFirstByTreeId(id);
            if (pageTree != null) {
                pageTreeRepository.delete(pageTree);
                onDeletePage(pageTree);
            }
        }
        return "redirect:/manage/page";
    }

    @PostMapping("/page/move-node")
    @ResponseBody
    public String moveNode(@RequestParam String id, @RequestParam int parentId, @RequestParam int position, Model model) {
        PageTreeModel pageTree;
        try {
            Integer intID = Integer.decode(id);
            pageTree = pageTreeRepository.findOne(intID);
        } catch (Exception e) {
            pageTree = pageTreeRepository.findFirstByTreeId(id);
        }
        if (pageTree != null) {
            pageTree.setParentId(parentId);
            pageTree.setPosition(position);
            pageTreeRepository.save(pageTree);
        }
        return "redirect:/manage/page";
    }

    private void addFolders(PageTreeModel pageTree) {
        if (pageTree.getType() == 0) {
            FoldersModel foldersModel = new FoldersModel();
            foldersModel.setTitle(pageTree.getText());
            foldersModel.setFolder(pageTree);
            foldersRepository.save(foldersModel);
        }
    }

    private void editFolders(PageTreeModel pageTree) {
        if (pageTree.getType() == 0) {
            FoldersModel foldersModel = foldersRepository.findFirstByFolderId(pageTree.getId());
            foldersModel.setTitle(pageTree.getText());
            foldersRepository.save(foldersModel);
        }
    }

    private void deleteFolders(PageTreeModel pageTree) {
        if (pageTree.getType() == 0) {
            FoldersModel foldersModel = foldersRepository.findFirstByFolderId(pageTree.getId());
            foldersRepository.delete(foldersModel);
        }
    }

    private void onDeletePage(PageTreeModel pageTree) {
        deleteFolders(pageTree);
        if (pageTree.getType() == 1) {
            PageModel pageModel = pageRepository.findOne(pageTree.getId());
            togglesRepository.delete(pageModel.getToggles());
//            pageModel.getToggles().forEach(tm -> {
//                togglesRepository.delete(tm);
//            });
            pageRepository.delete(pageModel);
        }
    }

    @GetMapping("/page/page-edit/{pageId}")
    public String editPage(@PathVariable String pageId, Model model) {
//        if (!model.containsAttribute("page")) {

        PageModel page;
        try {
            Integer intPageId = Integer.decode(pageId);
            page = pageRepository.findOne(intPageId);
        } catch (Exception e) {
            PageTreeModel pageTree = pageTreeRepository.findFirstByTreeId(pageId);
            page = pageRepository.findOne(pageTree.getId());
        }

        model.addAttribute("page", page);
        if (page.getImage() != null) {
            String img = Base64.getEncoder().encodeToString(page.getImage());
            model.addAttribute("imgVis", img);
        }
        return "admin/manage/page/page-edit";
    }

    @PostMapping(path = {"/page/page-edit"})
    public String save(@ModelAttribute("page") PageModel page, @RequestParam("attFile") MultipartFile attFile,
                       @RequestParam("imageFile") MultipartFile imageFile, Model model) {

        if (!attFile.isEmpty()) {
            page.setAttachmentName(StringUtils.cleanPath(attFile.getOriginalFilename()));
            page.setAttachmentType(attFile.getContentType());
            page.setAttachment(storeFile(attFile));
        }
        if (!imageFile.isEmpty()) {
            byte[] image = storeFile(imageFile);
            String img = Base64.getEncoder().encodeToString(image);
            model.addAttribute("imgVis", img);
            page.setImage(image);
        }
        pageRepository.save(page);
        model.addAttribute("page", page);
        return "redirect:/manage/page/page-edit/" + page.getId();
    }

    @PostMapping(path = {"/page/page-edit/rem-tog"})
    public String remToggles(@ModelAttribute("page") PageModel page, @ModelAttribute("remIndex") int remIndex, Model model) {
        togglesRepository.delete(page.getToggles().get(remIndex));
        page.getToggles().remove(remIndex);
        model.addAttribute("page", page);
        return "redirect:/manage/page/page-edit/" + page.getId();
    }

    @PostMapping(path = {"/page/page-edit/add-tog"})
    public String addToggles(@ModelAttribute("page") PageModel page, @ModelAttribute("title") String title, @ModelAttribute("content") String content,
                             @ModelAttribute("togglesId") String togglesId, @RequestParam("togglesImage") MultipartFile togglesImage, Model model) {
        TogglesModel togglesModel = new TogglesModel();
        try {
            togglesModel = togglesRepository.findOne(Integer.decode(togglesId));
        } catch (Exception e) {

        }
        togglesModel.setPage(page);
        togglesModel.setTitle(title);
        togglesModel.setContent(content);
        if (!togglesImage.isEmpty()) {
            togglesModel.setImage(storeFile(togglesImage));
        }
        togglesRepository.save(togglesModel);
        model.addAttribute("page", page);
        return "redirect:/manage/page/page-edit/" + page.getId();
    }

    @GetMapping("/page/page-edit/download/{pageId}")
    public ResponseEntity<Resource> download(@PathVariable Integer pageId, Model model) {
        PageModel page = pageRepository.findOne(pageId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(page.getAttachmentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + page.getAttachmentName() + "\"")
                .body(new ByteArrayResource(page.getAttachment()));
    }

    @PostMapping(path = {"/page/page-edit/rem-image"})
    public String remImage(@ModelAttribute("page") PageModel page, Model model) {
        page.setImage(null);
        model.addAttribute("page", page);
        return "redirect:/manage/page/page-edit/" + page.getId();
    }

    @PostMapping(path = {"/page/page-edit/rem-att"})
    public String remAtt(@ModelAttribute("page") PageModel page, Model model) {
        page.setAttachment(null);
        page.setAttachmentType("");
        page.setAttachmentName("");
        model.addAttribute("page", page);
        return "redirect:/manage/page/page-edit/" + page.getId();
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
