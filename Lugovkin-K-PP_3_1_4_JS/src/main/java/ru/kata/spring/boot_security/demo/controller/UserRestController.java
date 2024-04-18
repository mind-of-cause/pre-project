package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Secured({"ROLE_ADMIN","ROLE_USER"})
public class UserRestController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        if (users == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userService.findUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User currentUser = userService.findByUsername(authentication.getName());
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        currentUser.setPassword(null);
        return ResponseEntity.ok(currentUser);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user, @RequestParam Set<Long> rolesIds) {
        Set<Role> selectedRoles = roleService.findByIds(rolesIds);
        if (selectedRoles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        userService.saveUser(user, selectedRoles);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUserPost(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);

        System.out.println(user);

        if (id == null || !id.equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Set<Long> rolesIds = user.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toSet());

        Set<Role> updatedRoles = roleService.findByIds(rolesIds);
        if (updatedRoles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        User updatedUser = userService.updateUser(id, user, updatedRoles);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestParam Long id) {
        try {
            userService.removeUserById(id);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<Role>> getAllRoles(Authentication authentication) {
        Set<Role> allRoles = roleService.findAllRoles();
        return ResponseEntity.ok(allRoles);
    }
}