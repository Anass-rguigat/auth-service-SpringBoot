package com.auth.demo.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.auth.demo.entity.AppUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountService accountService;

    public UserDetailsServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = accountService.loadUserByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("User not found in the database");
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        // Ajouter les rôles (avec préfixe ROLE_)
        authorities.addAll(
            appUser.getAppRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toSet())
        );

        // Ajouter les permissions des rôles
        authorities.addAll(
            appUser.getAppRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(perm -> new SimpleGrantedAuthority(perm.getName()))
                .collect(Collectors.toSet())
        );

        return new User(
            appUser.getUsername(),
            appUser.getPassword(),
            authorities
        );
    }
}
