package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.model.RoleEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse findById(Long id) {
        return toResponse(findEntityById(id));
    }

    public UserResponse save(RegisterRequest request) {
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        return toResponse(userRepository.save(user));
    }

    public UserResponse update(Long id, UpdateUserRequest request) {
        UserEntity existing = findEntityById(id);
        existing.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existing.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        }
        if (request.getRoleId() != null) {
            RoleEntity role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found with id: " + request.getRoleId()));
            existing.setRole(role);
        }
        return toResponse(userRepository.save(existing));
    }

    public void delete(Long id) {
        findEntityById(id);
        userRepository.deleteById(id);
    }

    private UserEntity findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    private UserResponse toResponse(UserEntity user) {
        String roleName = user.getRole() != null ? user.getRole().getRoleName() : null;
        return new UserResponse(user.getId(), user.getUsername(), roleName);
    }
}
