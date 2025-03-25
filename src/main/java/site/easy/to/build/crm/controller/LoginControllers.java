package site.easy.to.build.crm.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.service.myservice.LoginService;

@RestController
@RequestMapping("/api")
public class LoginControllers {

    @Autowired
    private LoginService loginService;

    @GetMapping("/checkRole")
    public boolean hasRoleManager(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Récupère la session sans en créer une nouvelle
        return loginService.hasRoleManager(session);
    }
}

