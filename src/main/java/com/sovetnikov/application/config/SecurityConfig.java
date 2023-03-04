package com.sovetnikov.application.config;


import com.sovetnikov.application.model.User;
import com.sovetnikov.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;
import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;

    @Autowired
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            Optional<User> optionalUser = userService.getByEmail(email);

            if(optionalUser.isEmpty()){
                throw new UsernameNotFoundException("Пользователь не существует");
            }

            return new org.springframework.security.core.userdetails.User(
                    optionalUser.get().getEmail(),
                    optionalUser.get().getPassword(),
                    Collections.emptyList());
        };
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/login").anonymous()
                .requestMatchers(HttpMethod.POST, "/api/login/register").anonymous()
                .anyRequest().authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/api/users", true);

        return http.build();
    }
}
