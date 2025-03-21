package site.easy.to.build.crm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.service.reset.DatabaseResetService;

@RestController
@RequestMapping("/")
public class DatabaseResetController {

    private final DatabaseResetService databaseResetService;

    public DatabaseResetController(DatabaseResetService databaseResetService) {
        this.databaseResetService = databaseResetService;
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetDatabase() {
        databaseResetService.resetDatabase();
        return ResponseEntity.ok("Base de données réinitialisée avec succès !");
    }
}