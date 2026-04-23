package com.example.demo.config;

import com.example.demo.model.Role;
import com.example.demo.model.RoleEntity;
import com.example.demo.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for(Role role: Role.values()) {
            RoleEntity roleEntity = roleRepository.findByRoleName(role.name()).orElse(null);
            if(roleEntity == null) {
                RoleEntity roleCreated = new RoleEntity();
                roleCreated.setRoleName(role.name());
                roleRepository.save(roleCreated);
            }
        }
    }
}
