package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RequestMapping("/admin")
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

    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAllRoles());
        return "/new";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        User user = userService.getUserById(id);
        Set<Role> allRoles = roleService.findAllRoles();
        Set<Role> userRoles = user.getRoles();

        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("userRoles", userRoles);
        return "edit";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user, @RequestParam Set<Long> rolesIds) {
        Set<Role> selectedRoles = roleService.findByIds(rolesIds);
        userService.saveUser(user, selectedRoles);
        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam("id") Long id, @ModelAttribute("user") User user, @RequestParam Set<Long> rolesIds) {
        Set<Role> updatedRoles = roleService.findByIds(rolesIds);
        userService.updateUser(id, user, updatedRoles);
        return "redirect:/admin";
    }

    @GetMapping("/roles")
    @ResponseBody
    public Set<Role> getAllRoles() {
        return roleService.findAllRoles();
    }

    @PostMapping("/delete")

    public String deleteUser(@RequestParam("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }
}
