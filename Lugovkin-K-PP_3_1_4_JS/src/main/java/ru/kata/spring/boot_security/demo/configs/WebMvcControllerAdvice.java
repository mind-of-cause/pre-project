package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Set;

@ControllerAdvice
public class WebMvcControllerAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "roles", new CustomCollectionEditor(Set.class) {
            @Override
            protected Object convertElement(Object element) {
                if (element instanceof String) {

                    String role = (String) element;
                    return new Role(role);
                }
                return null;
            }
        });
    }
}