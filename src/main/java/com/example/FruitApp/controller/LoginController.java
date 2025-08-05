package com.example.FruitApp.controller;

import com.example.FruitApp.dto.UserLoginDto;
import com.example.FruitApp.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService authenticationService;

    @PostMapping
    public ResponseEntity<Object> auth(@RequestBody UserLoginDto userRequest) {
        return ResponseEntity.ok(authenticationService.auth(userRequest));
    }
}
