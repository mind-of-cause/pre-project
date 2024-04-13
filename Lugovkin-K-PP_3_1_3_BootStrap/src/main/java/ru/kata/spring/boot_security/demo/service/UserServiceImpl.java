package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User findByUsername(String username){
    return userRepository.findByUsername(username);
    }
    public void saveUser(User user, Set<Role> roles) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword("{bcrypt}" + encodedPassword);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Transactional
    public void removeUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void updateUser(Long id, User updatedUser, Set<Role> roles) {
        User userToUpdate = userRepository.findById(id).orElse(null);

        if (userToUpdate != null) {
            userToUpdate.setUsername(updatedUser.getUsername());
            userToUpdate.setEmail(updatedUser.getEmail());

            if(!passwordEncoder.matches(updatedUser.getPassword(), userToUpdate.getPassword())) {
                String newEncodedPassword = passwordEncoder.encode(updatedUser.getPassword());
                userToUpdate.setPassword("{bcrypt}" + newEncodedPassword);
            }
            userToUpdate.setRoles(roles);
            userRepository.save(userToUpdate);
        }
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = findByUsername(username);
    if(user == null){
        throw new UsernameNotFoundException(String.format("User'%s' not found", username ));
    }
    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
            mapRolesToAuthorities(user.getRoles()));
    }
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
    return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

}