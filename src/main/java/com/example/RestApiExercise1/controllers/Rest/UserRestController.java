package com.example.RestApiExercise1.controllers.Rest;

import com.example.RestApiExercise1.models.User;
import com.example.RestApiExercise1.models.dto.UserDTO;
import com.example.RestApiExercise1.models.enums.Role;
import com.example.RestApiExercise1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/api/client")
@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Возвращает всех пользователей, но только имя и номер телефона
    @GetMapping("/all")
    public List<UserDTO> getAllUserInfo() {
        List<User> users = userRepository.findAll();
        // Преобразуем каждый объект User в UserDTO
        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getName()))
                .collect(Collectors.toList());
    }

    // Возвращает пользователя по его id
    @GetMapping("/get/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getName(),
                    user.getPhoneNumber(),
                    user.getEmail());
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Добавление нового пользователя
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Пользователь с таким email уже существует");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.getRoles().add(Role.ROLE_USER);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Пользователь успешно создан");
    }

    // Обновление данных пользователя (кроме id)
    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            log.debug("данные пользователя: {}", existingUser);
            // Обновляем поля, кроме id
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            userRepository.save(existingUser);
            return ResponseEntity.ok("Пользовательские данные успешно обновлены");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Пользователь не найден");
        }
    }
}
