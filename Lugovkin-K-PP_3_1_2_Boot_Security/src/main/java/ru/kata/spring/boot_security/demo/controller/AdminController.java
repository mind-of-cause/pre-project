package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
@RequestMapping("/admin")
@Controller
public class AdminController {

    private UserService userService;

@Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @Secured("ROLE_ADMIN")

    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/new")
    @Secured("ROLE_ADMIN")

    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "/new";
    }

    @GetMapping("/edit")
    @Secured("ROLE_ADMIN")

    public String editUser(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "edit";
    }

    @PostMapping("/create")
    @Secured("ROLE_ADMIN")
    public String createUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    @Secured("ROLE_ADMIN")

    public String updateUser(@RequestParam("id") Long id, @ModelAttribute("user") User user) {
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    @Secured("ROLE_ADMIN")

    public String deleteUser(@RequestParam("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }
}
