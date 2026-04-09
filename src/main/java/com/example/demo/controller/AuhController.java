package com.example.demo.controller;


import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.model.UserEntity;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuhController {
    private final UserService userService;
    private final AuthenticationManager manager;
    private final JwtService jwtService;

    public AuhController(UserService userService, AuthenticationManager manager, JwtService jwtService) {
        this.userService = userService;
        this.manager = manager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserEntity user) {
        // guardar el usuario
        userService.save(user);
        // retornar token para el usuario guardado
        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        UsernamePasswordAuthenticationToken auth
                = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authRespuesta = manager.authenticate(auth);
        if(authRespuesta.isAuthenticated()) {
            String token = jwtService.generateToken(username);
            return ResponseEntity.ok(new AuthResponse(token));
        }
        return ResponseEntity.badRequest().body(null);
    }

    // logout
}
