package com.example.FruitApp.service;

import com.example.FruitApp.dto.TokenDto;
import com.example.FruitApp.dto.userDto.UserSignUpDto;
import com.example.FruitApp.model.Statuses;
import com.example.FruitApp.model.User;
import com.example.FruitApp.repository.StatusRepository;
import com.example.FruitApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public Object save(UserSignUpDto userDto) {
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            return "Email artıq qeydiyyatdan keçib!";
        }

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            return "Şifrə və təsdiq şifrə uyğun deyil!";
        }

        Statuses defaultStatus = statusRepository.findByName("ONLINE")
                .orElseThrow(() -> new RuntimeException("Default status not found"));

        User user = User.builder()
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .statusId(defaultStatus)
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken = null;
        if (userDto.isRememberPassword()) {
            refreshToken = jwtService.generateRefreshToken(user);
        }

        return TokenDto.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}

