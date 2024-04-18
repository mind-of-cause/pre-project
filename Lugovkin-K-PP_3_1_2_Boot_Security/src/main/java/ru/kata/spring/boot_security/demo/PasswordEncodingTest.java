package ru.kata.spring.boot_security.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncodingTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("admin"));
    }
}
