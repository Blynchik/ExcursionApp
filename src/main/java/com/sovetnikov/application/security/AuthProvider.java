package com.sovetnikov.application.security;

import com.sovetnikov.application.service.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthProvider implements AuthenticationProvider {

    private final AppUserDetailsService appUserDetailsService;

    @Autowired
    public AuthProvider(AppUserDetailsService appUserDetailsService){
        this.appUserDetailsService = appUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();

        UserDetails userDetails = appUserDetailsService.loadUserByUsername(name);

        String password = authentication.getCredentials().toString();

        if(!password.equals(userDetails.getPassword()))
            throw new BadCredentialsException("Неверный пароль");

        return new UsernamePasswordAuthenticationToken(userDetails, password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
