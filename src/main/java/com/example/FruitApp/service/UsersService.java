package com.example.FruitApp.service;

import com.example.FruitApp.enums.Role;
import com.example.FruitApp.enums.Statuses;
import com.example.FruitApp.model.User;
import com.example.FruitApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UsersService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public List<User> getAllUser() {
        return userRepository.findAll();
    }



    public Object getUserById(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.ok("İstifadəçi tapılmadı!: ID = " + id);
        }
    }


    public ResponseEntity<String> updateUserById(UUID id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(updatedUser.getUsername());
            Optional<User> optionalUserByEmail = userRepository.findByEmail(updatedUser.getEmail());
            if (optionalUserByEmail.isPresent()) {
                return ResponseEntity.ok("Bu email artiq sistemde var!");
            }
            user.setEmail(updatedUser.getEmail());
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            user.setRole(Role.valueOf(updatedUser.getRole().toString().toUpperCase()));
            user.setStatus(Statuses.valueOf(updatedUser.getStatus().toString().toUpperCase()));
            userRepository.save(user);
            return ResponseEntity.ok("İstifadəçi uğurla yeniləndi.");
        } else {
            return ResponseEntity.ok("İstifadəçi tapılmadı.");
        }

    }

    public ResponseEntity<String> patchUserById(UUID id, Map<String, Object> updates) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                switch (key) {
                    case "username" -> user.setUsername((String) value);
                    case "email" -> {
                        String newEmail = (String) value;
                        Optional<User> emailOwner = userRepository.findByEmail(newEmail);
                        if (emailOwner.isPresent() && !emailOwner.get().getId().equals(user.getId())) {
                            return ResponseEntity.badRequest().body("Bu email artıq sistemdə mövcuddur!");
                        }
                        user.setEmail(newEmail);
                    }

                    case "password" -> user.setPassword(passwordEncoder.encode((String) value));
                    case "role" -> user.setRole(Role.valueOf(value.toString().toUpperCase()));
                    case "status" -> user.setStatus(Statuses.valueOf(value.toString().toUpperCase()));
                }
            }

            userRepository.save(user);
            return ResponseEntity.ok("İstifadəçi məlumatları qismən yeniləndi.");
        } else {
            return ResponseEntity.ok("İstifadəçi tapılmadı.");
        }
    }



    public String deleteUserById(UUID id) {
        if (!userRepository.existsById(id)) {
            return "Bu ID: " + id + " uzre istifadeci tapilmadi";
        }

        userRepository.deleteById(id);
        return "İstifadəçi silindi!";
    }

}