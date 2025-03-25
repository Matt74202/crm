package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import site.easy.to.build.crm.service.importt.ImportService;

@Controller
@RequestMapping("/import")
public class ImportController {

    private final ImportService importService;

    @Autowired
    public ImportController(ImportService importService) {
        this.importService = importService;
    }
    
    @GetMapping("/")
    public String index(){
        return "import/import";
    }

    @PostMapping("/employee")
    public String importEmployees(@RequestParam("filePath") MultipartFile filePath) {
        try {
            importService.importEmployees(filePath);
        } catch (Exception e){
            return "error/500";
        }
        return "redirect:/";
    }

}
