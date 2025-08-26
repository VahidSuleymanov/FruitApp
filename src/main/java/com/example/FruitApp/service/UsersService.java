package com.example.FruitApp.service;

import com.example.FruitApp.dto.userDto.UserUpdateDto;
import com.example.FruitApp.model.Statuses;
import com.example.FruitApp.model.User;
import com.example.FruitApp.repository.StatusRepository;
import com.example.FruitApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final StatusRepository statusRepository;

    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }



    public ResponseEntity<?> getUserById(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("İstifadəçi tapılmadı!: ID = " + id);
        }
    }



    public ResponseEntity<String> updateUserById(UUID id, UserUpdateDto updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Optional<User> optionalUserByEmail = userRepository.findByEmail(updatedUser.getEmail());
            if (optionalUserByEmail.isPresent() && !optionalUserByEmail.get().getId().equals(user.getId())) {
                return ResponseEntity.ok("Bu email artıq sistemdə var!");
            }

            user.setEmail(updatedUser.getEmail());
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

            Optional<Statuses> statusOptional = statusRepository.findById(updatedUser.getStatusId());
            if (statusOptional.isEmpty()) {
                return ResponseEntity.ok("Belə bir status mövcud deyil!");
            }
            user.setStatusId(statusOptional.get());

            userRepository.save(user);
            return ResponseEntity.ok("İstifadəçi uğurla yeniləndi!");
        } else {
            return ResponseEntity.ok("İstifadəçi tapılmadı!");
        }
    }



    public ResponseEntity<String> patchUserById(UUID id, Map<String, Object> updates) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.ok("İstifadəçi tapılmadı!");
        }

        User user = optionalUser.get();
        boolean updated = false;

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            switch (key) {
                case "email" -> {
                    if (value == null || !(value instanceof String) || ((String) value).isBlank()) {
                        break; // boş email gəlirsə dəyişmə
                    }
                    String newEmail = (String) value;
                    Optional<User> emailOwner = userRepository.findByEmail(newEmail);
                    if (emailOwner.isPresent() && !emailOwner.get().getId().equals(user.getId())) {
                        return ResponseEntity.badRequest().body("Bu email artıq sistemdə mövcuddur!");
                    }
                    user.setEmail(newEmail);
                    updated = true;
                }

                case "password" -> {
                    if (value == null || !(value instanceof String) || ((String) value).isBlank()) {
                        break; // boş şifrə gəlirsə dəyişmə
                    }
                    user.setPassword(passwordEncoder.encode((String) value));
                    updated = true;
                }

                case "statusId" -> {
                    if (value == null || value.toString().isBlank()) {
                        break; // boş statusId gəlirsə dəyişmə
                    }
                    try {
                        UUID statusId = UUID.fromString(value.toString());
                        Optional<Statuses> optionalStatus = statusRepository.findById(statusId);
                        if (optionalStatus.isEmpty()) {
                            return ResponseEntity.badRequest().body("Belə bir status mövcud deyil!");
                        }
                        user.setStatusId(optionalStatus.get());
                        updated = true;
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("Status ID düzgün formatda deyil!");
                    }
                }

                default -> {
                    return ResponseEntity.badRequest().body("Naməlum sahə: " + key);
                }
            }
        }

        if (updated) {
            userRepository.save(user);
            return ResponseEntity.ok("İstifadəçi məlumatları qismən yeniləndi!");
        } else {
            return ResponseEntity.badRequest().body("Heç bir məlumat yenilənmədi.");
        }
    }




    public String deleteUserById(UUID id) {
        if (!userRepository.existsById(id)) {
            return "Bu ID: " + id + " uzre istifadeci tapilmadi!";
        }

        userRepository.deleteById(id);
        return "İstifadəçi silindi!";
    }

}