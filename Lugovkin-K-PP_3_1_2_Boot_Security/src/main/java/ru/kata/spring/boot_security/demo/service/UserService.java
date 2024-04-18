package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    public void saveUser(User user, Set<Role> selectedRoles);
    public void removeUserById(Long id);
    public List<User> getAllUsers();
    public User getUserById(Long id);
    public void updateUser(Long id, User updatedUser, Set<Role> roles);

}
