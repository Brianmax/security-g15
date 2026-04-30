package com.example.demo;

import com.example.demo.model.PermissionEntity;
import com.example.demo.model.RoleEntity;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) {
        PermissionEntity readProduct   = findOrCreatePermission("READ_PRODUCT");
        PermissionEntity writeProduct  = findOrCreatePermission("WRITE_PRODUCT");
        PermissionEntity deleteProduct = findOrCreatePermission("DELETE_PRODUCT");
        PermissionEntity manageUsers   = findOrCreatePermission("MANAGE_USERS");

        findOrCreateRole("GUEST", Set.of(readProduct));
        findOrCreateRole("ADMIN", Set.of(readProduct, writeProduct, deleteProduct, manageUsers));
    }

    private PermissionEntity findOrCreatePermission(String name) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(new PermissionEntity(name)));
    }

    private void findOrCreateRole(String name, Set<PermissionEntity> permissions) {
        RoleEntity role = roleRepository.findByRoleName(name)
                .orElseGet(() -> {
                    RoleEntity r = new RoleEntity();
                    r.setRoleName(name);
                    return r;
                });
        role.setPermissions(permissions);
        roleRepository.save(role);
    }
}
