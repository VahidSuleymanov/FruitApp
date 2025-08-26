package com.example.FruitApp.controller;

import com.example.FruitApp.dto.userDto.UserLoginDto;
import com.example.FruitApp.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService authenticationService;

    @PostMapping
    public ResponseEntity<Object> auth(@Valid @RequestBody UserLoginDto userRequest) {
        return ResponseEntity.ok(authenticationService.auth(userRequest));
    }
}
