package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ru.kata.spring.boot_security.demo.service.UserService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(
            SuccessUserHandler successUserHandler,
            CustomLogoutSuccessHandler customLogoutSuccessHandler,
            UserService userService
            ) {
        this.successUserHandler = successUserHandler;
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        return provider;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .authorizeRequests()
                .antMatchers("/logout", "/login").permitAll()
                .antMatchers("/", "/index").permitAll()
                .antMatchers("/user/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(customLogoutSuccessHandler)

                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll();

    }
}