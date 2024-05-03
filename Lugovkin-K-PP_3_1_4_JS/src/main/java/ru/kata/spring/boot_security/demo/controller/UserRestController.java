package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@RestController
@RequestMapping("/api/user")

@Secured("ROLE_USER")
public class UserRestController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        if (currentUser == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        currentUser.setPassword(null);
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<Role>> getAllRoles(Authentication authentication) {
        Set<Role> allRoles = roleService.findAllRoles();
        return ResponseEntity.ok(allRoles);
    }
}
