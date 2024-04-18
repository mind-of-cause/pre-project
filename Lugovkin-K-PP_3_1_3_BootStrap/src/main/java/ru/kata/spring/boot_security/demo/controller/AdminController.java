package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;

@RequestMapping("/user")
@Controller
@Secured("ROLE_ADMIN")

public class AdminController {

    private UserService userService;
    private final RoleService roleService;


    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String listUsers(Model model, Authentication authentication) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        User userForm = new User();
        model.addAttribute("user", userForm);
        Set<Role> allRoles = roleService.findAllRoles();
        model.addAttribute("allRoles", allRoles);

        // Получение данных о текущем авторизованном пользователе
        String name = authentication.getName();
        User currentUser = userService.findByUsername(name);
        model.addAttribute("currentUser", currentUser);

        return "user";
    }


    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user, @RequestParam Set<Long> rolesIds) {
        Set<Role> selectedRoles = roleService.findByIds(rolesIds);
        userService.saveUser(user, selectedRoles);
        return "redirect:/user";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam("id") Long id, @ModelAttribute("user") User user, @RequestParam Set<Long> rolesIds) {
        Set<Role> updatedRoles = roleService.findByIds(rolesIds);
        userService.updateUser(id, user, updatedRoles);
        return "redirect:/user";
    }

    @GetMapping("/roles")
    @ResponseBody
    public Set<Role> getAllRoles() {
        return roleService.findAllRoles();
    }

    @PostMapping("/delete")

    public String deleteUser(@RequestParam("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/user";
    }
}
