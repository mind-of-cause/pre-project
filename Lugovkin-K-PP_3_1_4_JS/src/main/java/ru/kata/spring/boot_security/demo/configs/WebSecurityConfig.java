package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.service.UserService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final SuccessUserHandler successUserHandler;

    @Autowired
    public WebSecurityConfig(
            SuccessUserHandler successUserHandler,
            CustomLogoutSuccessHandler customLogoutSuccessHandler,
            UserService userService) {
        this.successUserHandler = successUserHandler;
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
        this.userService = userService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .authorizeRequests()
                .antMatchers("/logout", "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/roles").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/api/users/current").hasAnyRole("USER", "ADMIN")

                .antMatchers("/user").hasAnyRole("ADMIN","USER")
                .antMatchers("/api/users/**").hasAnyRole("ADMIN")
                .antMatchers("/api/user/**").hasAnyRole("USER")
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