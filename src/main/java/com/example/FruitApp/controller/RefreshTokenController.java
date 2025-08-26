package com.example.FruitApp.controller;

import com.example.FruitApp.dto.TokenDto;
import com.example.FruitApp.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/refreshToken")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping
    public TokenDto refreshToken(@RequestBody TokenDto tokenDto) {
        return refreshTokenService.saveRefreshToken(tokenDto);
    }
}

