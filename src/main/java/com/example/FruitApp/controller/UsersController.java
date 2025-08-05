package com.example.FruitApp.controller;

import com.example.FruitApp.model.User;
import com.example.FruitApp.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;


    @GetMapping
    public List<User> getAllUser() {
        return usersService.getAllUser();
    }

    @GetMapping("/{id}")
    public Object getUserById(@PathVariable UUID id) {
        return usersService.getUserById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable UUID id, @RequestBody User updatedUser) {
        return usersService.updateUserById(id, updatedUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchUser(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return usersService.patchUserById(id, updates);
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable UUID id) {
        return usersService.deleteUserById(id);
    }
}
