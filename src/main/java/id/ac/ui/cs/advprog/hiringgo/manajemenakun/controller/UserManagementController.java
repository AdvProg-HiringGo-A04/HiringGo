package id.ac.ui.cs.advprog.hiringgo.manajemenakun.controller;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserAccountDTO;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/admin/users")
public class UserManagementController {

    private static final Logger LOGGER = Logger.getLogger(UserManagementController.class.getName());
    private final UserManagementService userManagementService;

    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping
    public String listUsers(Model model) {
        try {
            List<UserAccountDTO> userAccounts = userManagementService.getAllUserAccounts();
            model.addAttribute("userAccounts", userAccounts);
            return "user/list";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user accounts", e);
            model.addAttribute("errorMessage", "Terjadi kesalahan saat mengambil data akun pengguna");
            return "error";
        }
    }
}