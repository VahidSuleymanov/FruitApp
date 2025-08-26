package com.example.FruitApp.service;

import com.example.FruitApp.dto.TokenDto;
import com.example.FruitApp.model.*;
import com.example.FruitApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public TokenDto saveRefreshToken(TokenDto tokenDto) {
        String refreshToken = tokenDto.getRefreshToken();

        if (!StringUtils.hasText(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token boş ola bilməz");
        }

        String email = jwtService.findUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tapılmadı"));

        if (!jwtService.tokenControl(refreshToken, user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token etibarsızdır");
        }

        String newAccessToken = jwtService.generateAccessToken(user);

        return TokenDto.builder()
                .token(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
