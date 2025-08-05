package com.example.FruitApp.service;

import com.example.FruitApp.dto.UserLoginDto;
import com.example.FruitApp.dto.TokenDto;
import com.example.FruitApp.model.User;
import com.example.FruitApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;


    public Object auth(UserLoginDto userRequest) {
        Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
        if (existingUser.isEmpty()) {
            return "Email tapilmadi!";
        }

        User user = existingUser.get();
        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            return "Şifrə yanlışdır!";
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getEmail(),
                        userRequest.getPassword()
                )
        );


        String token = jwtService.generateToken(user);

        return TokenDto.builder().token(token).build();
    }
}
