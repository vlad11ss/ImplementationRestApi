package com.example.RestApiExercise1.services;

import com.example.RestApiExercise1.models.User;
import com.example.RestApiExercise1.models.enums.Role;
import com.example.RestApiExercise1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public boolean createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) return false;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.getRoles().add(Role.ROLE_USER); // Добавление роли USER
        userRepository.save(user);
        return true;
    }
}
