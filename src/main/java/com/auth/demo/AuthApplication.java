package com.auth.demo;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.auth.demo.config.RsaKeysConfig;
import com.auth.demo.constant.PermissionList;
import com.auth.demo.entity.AppRole;
import com.auth.demo.entity.AppUser;
import com.auth.demo.entity.Permission;
import com.auth.demo.repository.PermissionRepository;
import com.auth.demo.service.AccountService;
import com.auth.demo.service.RoleService;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeysConfig.class)
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    CommandLineRunner start(AccountService accountService, PermissionRepository permissionRepository, RoleService roleService) {
        return args -> {

            // 1. Seed Permissions
            for (String permName : PermissionList.DEFAULT_PERMISSIONS) {
                if (permissionRepository.findByName(permName) == null) {
                    permissionRepository.save(new Permission(null, permName));
                }
            }

            // 2. Seed Roles
            roleService.addNewRole(new AppRole(null, "USER", null));
            roleService.addNewRole(new AppRole(null, "ADMIN", null));
            roleService.addNewRole(new AppRole(null, "CUSTOMER_MANAGER", null));
            roleService.addNewRole(new AppRole(null, "PRODUCT_MANAGER", null));
            roleService.addNewRole(new AppRole(null, "BILLS_MANAGER", null));

            // 3. Seed Users
            AppUser anass = accountService.addNewUser(new AppUser(null,
                    "Anass", "Ggg", "anass.ggg",
                    "anass.ggg@example.com", "Aa123456",
                    "+212600000001", "Rabat, Morocco", "Male",
                    LocalDate.of(1995, 7, 3), Instant.now(),
                    new ArrayList<>()));

            AppUser sofia = accountService.addNewUser(new AppUser(null,
                    "Sofia", "Benali", "sofia.benali",
                    "sofia.benali@example.com", "Aa123456",
                    "+212600000002", "Casablanca, Morocco", "Female",
                    LocalDate.of(1990, 11, 12), Instant.now(),
                    new ArrayList<>()));

            AppUser youssef = accountService.addNewUser(new AppUser(null,
                    "Youssef", "El Amrani", "youssef.elamrani",
                    "youssef.elamrani@example.com", "Aa123456",
                    "+212600000003", "Fes, Morocco", "Male",
                    LocalDate.of(1988, 4, 20), Instant.now(),
                    new ArrayList<>()));

            AppUser imane = accountService.addNewUser(new AppUser(null,
                    "Imane", "Charki", "imane.charki",
                    "imane.charki@example.com", "Aa123456",
                    "+212600000004", "Marrakech, Morocco", "Female",
                    LocalDate.of(1992, 3, 8), Instant.now(),
                    new ArrayList<>()));

            AppUser admin = accountService.addNewUser(new AppUser(null,
                    "Admin", "User", "admin.user",
                    "admin.user@example.com", "Aa123456",
                    "+212600000000", "Casablanca, Morocco", "Male",
                    LocalDate.of(1985, 1, 1), Instant.now(),
                    new ArrayList<>()));

            // 4. Récupérer les rôles par leur nom (on peut supposer que les rôles sont déjà créés)
            AppRole userRole = roleService.findRoleByName("USER");
            AppRole adminRole = roleService.findRoleByName("ADMIN");
            AppRole customerManagerRole = roleService.findRoleByName("CUSTOMER_MANAGER");
            AppRole productManagerRole = roleService.findRoleByName("PRODUCT_MANAGER");
            AppRole billsManagerRole = roleService.findRoleByName("BILLS_MANAGER");

            // 5. Assigner les rôles par ID (avec la nouvelle méthode)
            accountService.addRoleToUserById(anass.getId(), userRole.getId());
            accountService.addRoleToUserById(admin.getId(), userRole.getId());
            accountService.addRoleToUserById(admin.getId(), adminRole.getId());
            accountService.addRoleToUserById(sofia.getId(), userRole.getId());
            accountService.addRoleToUserById(sofia.getId(), customerManagerRole.getId());
            accountService.addRoleToUserById(youssef.getId(), userRole.getId());
            accountService.addRoleToUserById(youssef.getId(), productManagerRole.getId());
            accountService.addRoleToUserById(imane.getId(), userRole.getId());
            accountService.addRoleToUserById(imane.getId(), billsManagerRole.getId());
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
