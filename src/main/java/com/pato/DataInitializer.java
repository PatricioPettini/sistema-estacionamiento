package com.pato;

import com.pato.model.Role;
import com.pato.model.UserSec;
import com.pato.repository.IRoleRepository;
import com.pato.repository.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;

@Component
@Profile("!test")
@Transactional
public class DataInitializer implements CommandLineRunner {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    public DataInitializer(IUserRepository userRepository, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ADMIN");
                    return roleRepository.save(role);
                });

        userRepository.findByUsername("admin").orElseGet(() -> {
            UserSec admin = new UserSec();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin"));
            admin.setEnabled(true);
            admin.setAccountNotExpired(true);
            admin.setCredentialNotExpired(true);
            admin.setAccountNotLocked(true);
            admin.setRolesList(Set.of(adminRole));
            return userRepository.save(admin);
        });
    }
}
