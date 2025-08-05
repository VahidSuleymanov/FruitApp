package com.example.FruitApp.service;

import com.example.FruitApp.dto.TokenDto;
import com.example.FruitApp.dto.UserSignUpDto;
import com.example.FruitApp.enums.Statuses;
import com.example.FruitApp.model.User;
import com.example.FruitApp.enums.Role;
import com.example.FruitApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;;


    public Object save(UserSignUpDto userDto) {
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            return "Email artıq qeydiyyatdan keçib!";
        }

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            return "Şifrə və təsdiq şifrə uyğun deyil!";
        }

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .rememberPassword(userDto.isRememberPassword())
                .role(Role.USER)
                .status(Statuses.ONLINE)
                .build();

        userRepository.save(user);

        var token = jwtService.generateToken(user);

        return TokenDto.builder().token(token).build();
    }


}
