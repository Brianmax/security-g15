package com.example.demo.security;

import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        UserEntity userEntity = userOptional.get();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority roleUser = new SimpleGrantedAuthority(userEntity.getRole().getRoleName());
        authorities.add(roleUser);
        return new User(userEntity.getUsername(), userEntity.getPassword(), authorities);
    }
}
