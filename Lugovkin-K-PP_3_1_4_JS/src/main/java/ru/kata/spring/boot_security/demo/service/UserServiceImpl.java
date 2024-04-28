package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public void saveUser(User user, Set<Role> roles) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void removeUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));
        user.getRoles().clear();
        userRepository.save(user);
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User updateUser(Long id, User updatedUser, Set<Role> roles) {
        User userToUpdate = userRepository.findById(id).orElse(null);

        if (userToUpdate == null) {
            return null;
        }

        userToUpdate.setUsername(updatedUser.getUsername());
        userToUpdate.setEmail(updatedUser.getEmail());

        if (!passwordEncoder.matches(updatedUser.getPassword(), userToUpdate.getPassword())) {
            String newEncodedPassword = passwordEncoder.encode(updatedUser.getPassword());
            userToUpdate.setPassword(newEncodedPassword);
        }

        userToUpdate.setRoles(roles);
        return userRepository.save(userToUpdate);
    }

    @Override
    @Transactional

    public User findUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.getRoles().size();
        }
        return user;

    }


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User'%s' not found", username));
        }
        return user;
    }

}
