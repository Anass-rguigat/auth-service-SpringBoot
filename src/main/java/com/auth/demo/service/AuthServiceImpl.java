package com.auth.demo.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.auth.demo.entity.AppUser;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;

    public AuthServiceImpl(JwtEncoder jwtEncoder,
                           JwtDecoder jwtDecoder,
                           UserDetailsService userDetailsService,
                           AuthenticationManager authenticationManager,
                           AccountService accountService) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
    }

    @Override
    public Map<String, Object> login(String grantType,
                                     String username,
                                     String password,
                                     boolean withRefreshToken,
                                     String refreshToken) throws Exception {

        if (grantType == null || grantType.isBlank()) {
            throw new IllegalArgumentException("Le type de connexion (grantType) est requis.");
        }

        String subject;

        switch (grantType) {
            case "password" -> {
                if (username == null || username.isEmpty()) {
                    throw new IllegalArgumentException("Le nom d'utilisateur est requis.");
                }
                if (password == null || password.isEmpty()) {
                    throw new IllegalArgumentException("Le mot de passe est requis.");
                }
                try {
                    Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                    );
                    subject = authentication.getName();
                } catch (Exception e) {
                    throw new IllegalArgumentException("Nom d'utilisateur ou mot de passe invalide.");
                }
            }
            case "refreshToken" -> {
                if (refreshToken == null || refreshToken.isBlank()) {
                    throw new IllegalArgumentException("Le jeton de rafraîchissement est requis.");
                }
                Jwt decodedJwt;
                try {
                    decodedJwt = jwtDecoder.decode(refreshToken);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Jeton de rafraîchissement invalide ou expiré.");
                }
                subject = decodedJwt.getSubject();
                if (subject == null || subject.isBlank()) {
                    throw new IllegalArgumentException("Le sujet est manquant dans le jeton de rafraîchissement.");
                }
                userDetailsService.loadUserByUsername(subject);
            }
            default -> throw new IllegalArgumentException("Type de connexion non supporté : " + grantType);
        }

        AppUser user = accountService.loadUserByUsername(subject);

        Set<String> roles = user.getAppRoles().stream()
                .map(r -> "ROLE_" + r.getRoleName())
                .collect(Collectors.toSet());

        Set<String> permissions = user.getAppRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(p -> p.getName())
                .collect(Collectors.toSet());

        Map<String, Object> authoritiesMap = new HashMap<>();
        authoritiesMap.put("roles", roles);
        authoritiesMap.put("permissions", permissions);

        Instant now = Instant.now();
        Instant accessExpiration = now.plus(withRefreshToken ? 5 : 30, ChronoUnit.MINUTES);

        JwtClaimsSet accessClaims = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(now)
                .expiresAt(accessExpiration)
                .issuer("security-service")
                .claim("authorities", authoritiesMap)
                .build();

        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(accessClaims)).getTokenValue();

        String refreshTokenValue = null;
        if (withRefreshToken) {
            Instant refreshExpiration = now.plus(30, ChronoUnit.MINUTES);
            JwtClaimsSet refreshClaims = JwtClaimsSet.builder()
                    .subject(subject)
                    .issuedAt(now)
                    .expiresAt(refreshExpiration)
                    .issuer("security-service")
                    .build();
            refreshTokenValue = jwtEncoder.encode(JwtEncoderParameters.from(refreshClaims)).getTokenValue();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", accessToken);
        if (refreshTokenValue != null) data.put("refreshToken", refreshTokenValue);
        data.put("username", user.getUsername());
        data.put("roles", roles);
        data.put("rolePermissions", permissions);

        return data;
    }
}
