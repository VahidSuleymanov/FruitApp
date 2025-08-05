package com.example.FruitApp.controller;

import com.example.FruitApp.dto.UserSignUpDto;
import com.example.FruitApp.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/signUp")
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody UserSignUpDto userDto) {
        return ResponseEntity.ok(signUpService.save(userDto));
    }
}
