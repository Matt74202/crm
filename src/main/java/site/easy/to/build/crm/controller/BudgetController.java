package site.easy.to.build.crm.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.AuthorizationUtil;


@Controller
@RequestMapping("/manager/budget")
public class BudgetController {

    private final AuthenticationUtils authenticationUtils;
    private final UserService userService;
    private final CustomerService customerService;
    private final BudgetService budgetService;

    @Autowired
    public BudgetController(AuthenticationUtils authenticationUtils, UserService userService, CustomerService customerService,
        BudgetService budgetService) {
        this.authenticationUtils = authenticationUtils;
        this.userService= userService;
        this.customerService= customerService;
        this.budgetService= budgetService;
    }
    

    @GetMapping("/create-budget")
    public String showBudgetCreationForm(Model model, Authentication authentication) {

        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);

        if (user.isInactiveUser()) {
            return "error/account-inactive";
        }

        List<Customer> customers;
        if (AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            customers = customerService.findAll();
        } else {
            customers = customerService.findByUserId(user.getId());
        }

        model.addAttribute("customers", customers);
        model.addAttribute("budget", new Budget());

        return "budget/create-budget";
    }

    @PostMapping("/create-budget")
    public String createBudget(
            @ModelAttribute("budget") @Validated Budget budget,
            BindingResult bindingResult,
            @RequestParam("customerId") int customerId,
            Model model,
            Authentication authentication) {

        // Récupérer l'utilisateur connecté
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);

        // Vérifier si l'utilisateur est inactif
        if (user.isInactiveUser()) {
            return "error/account-inactive";
        }

        // Vérifier les erreurs de validation
        if (bindingResult.hasErrors()) {
            List<Customer> customers;
            if (AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
                customers = customerService.findAll();
            } else {
                customers = customerService.findByUserId(user.getId());
            }
            model.addAttribute("customers", customers);
            return "budget/create-budget";
        }

        // Récupérer le client sélectionné
        Customer customer = customerService.findByCustomerId(customerId);
        if (customer == null) {
            return "error/500"; // Client introuvable
        }

        // Vérifier les permissions pour un employé
        if (AuthorizationUtil.hasRole(authentication, "ROLE_EMPLOYEE")) {
            if (customer.getUser().getId() != userId) {
                return "error/500"; // Accès non autorisé
            }
        }

        // Compléter et sauvegarder le budget
        budget.setCustomer(customer);
        budget.setBudgetDate(LocalDateTime.now());
        budgetService.save(budget);

        // Rediriger vers la liste des budgets
        return "redirect:/manager/budget/all-budget";
    }

    @GetMapping("/all-budget")
    public String showAllBudgets(Model model) {
        List<Budget> budgets = budgetService.findAll();
        model.addAttribute("budgets",budgets);
        return "budget/all-budget";
    }
}
