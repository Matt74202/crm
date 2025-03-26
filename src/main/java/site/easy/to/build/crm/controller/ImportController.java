package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import site.easy.to.build.crm.entity.ImportResult;
import site.easy.to.build.crm.service.importt.BudgetImportService;
import site.easy.to.build.crm.service.importt.CustomerImportService;
import site.easy.to.build.crm.service.importt.ExpenseImportService;
import site.easy.to.build.crm.service.importt.ImportService;

@Controller
@RequestMapping("/import")
public class ImportController {

    private final ImportService importService;
    private final CustomerImportService customerImportService;
    private final BudgetImportService budgetImportService;
    private final ExpenseImportService expenseImportService;

    @Autowired
    public ImportController(ImportService importService, CustomerImportService customerImportService,
                            BudgetImportService budgetImportService, ExpenseImportService expenseImportService) {
        this.importService = importService;
        this.customerImportService = customerImportService;
        this.budgetImportService = budgetImportService;
        this.expenseImportService = expenseImportService;
    }

    @GetMapping("/")
    public String index() {
        return "import/import";
    }

    @PostMapping("/all")
    public String handleImport(
            @RequestParam("customerCsv") MultipartFile customerCsv,
            @RequestParam("budgetCsv") MultipartFile budgetCsv,
            @RequestParam("expenseCsv") MultipartFile expenseCsv,
            Model model) throws Exception {

        ImportService.ImportResult result = importService.importAll(customerCsv, budgetCsv, expenseCsv);

        if (result.isSuccess()) {
            model.addAttribute("successMessage", "Importations des tables réussies !");
        } else {
            model.addAttribute("errorMessage", result.getMessage());
        }

        return "import/import"; 
    }


    @PostMapping("/customer")
    public String importCustomers(@RequestParam("customerCsv") MultipartFile customerCsv, Model model) {
        try {
            site.easy.to.build.crm.service.importt.CustomerImportService.ImportResult result = customerImportService.importCustomers(customerCsv);
            model.addAttribute("importResult", result);
            if (result.isSuccess()) {
                model.addAttribute("successMessage", result.getMessage());
            } else {
                model.addAttribute("errorMessage", result.getMessage());
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite : " + e.getMessage());
        }
        return "import/import"; 
    }

    @PostMapping("/budget")
    public String importbudgets(@RequestParam("budgetCsv") MultipartFile budgetCsv, Model model) {
        try {
            site.easy.to.build.crm.entity.ImportResult result = budgetImportService.importBudgets(budgetCsv);
            model.addAttribute("importResult", result);
            if (result.isSuccess()) {
                model.addAttribute("successMessage", result.getMessage());
            } else {
                model.addAttribute("errorMessage", result.getMessage());
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite : " + e.getMessage());
        }
        return "import/import"; 
    }

    @PostMapping("/expense")
    public String importExpenses(@RequestParam("expenseCsv") MultipartFile expenseCsv, Model model) {
        try {
            site.easy.to.build.crm.entity.ImportResult result = expenseImportService.importExpenses(expenseCsv);
            model.addAttribute("importResult", result);
            if (result.isSuccess()) {
                model.addAttribute("successMessage", result.getMessage());
            } else {
                model.addAttribute("errorMessage", result.getMessage());
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite : " + e.getMessage());
        }
        return "import/import"; 
    }


}