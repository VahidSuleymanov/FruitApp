package com.example.FruitApp.service;

import com.example.FruitApp.dto.forgotPasswordDto.ForgotPasswordTokenResponseDto;
import com.example.FruitApp.dto.forgotPasswordDto.OtpVerifyRequestDto;
import com.example.FruitApp.dto.forgotPasswordDto.ResetPasswordRequestDto;
import com.example.FruitApp.model.OtpResetPassword;
import com.example.FruitApp.model.User;
import com.example.FruitApp.repository.OtpResetPasswordRepository;
import com.example.FruitApp.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final OtpResetPasswordRepository otpRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public ForgotPasswordTokenResponseDto requestReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        otpRepository.findByUserId(user).ifPresent(existingOtp -> {
            if (existingOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
                otpRepository.delete(existingOtp);
            } else {
                otpRepository.delete(existingOtp);
            }
        });

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        otpRepository.deleteByUserId(user);
        otpRepository.save(new OtpResetPassword(user, otp, LocalDateTime.now().plusMinutes(5)));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Forgot Password OTP");
        message.setText("Sizin OTP kodunuz: " + otp);
        mailSender.send(message);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId().toString());
        claims.put("purpose", "OTP_VERIFY");

        String token = jwtService.generateTokenWithClaims(user.getEmail(), claims);

        return new ForgotPasswordTokenResponseDto(token);
    }

    public ForgotPasswordTokenResponseDto verifyOtp(HttpServletRequest request, OtpVerifyRequestDto otpRequest) {
        String oldToken = request.getHeader("Authorization").substring(7);
        UUID userId = UUID.fromString(jwtService.extractUserId(oldToken));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String purpose = jwtService.extractClaim(oldToken, claims -> claims.get("purpose", String.class));
        if (!"OTP_VERIFY".equals(purpose)) {
            throw new RuntimeException("Invalid token type");
        }

        OtpResetPassword otpEntity = otpRepository.findByUserId(user)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (!otpEntity.getOtp().equals(otpRequest.getOtp()) || otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        otpRepository.delete(otpEntity);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());
        claims.put("purpose", "PASSWORD_RESET");

        String resetToken = jwtService.generateTokenWithClaims(jwtService.extractEmail(oldToken), claims);

        return new ForgotPasswordTokenResponseDto(resetToken);
    }

    public String resetPassword(HttpServletRequest request, ResetPasswordRequestDto resetRequest) {
        if (!resetRequest.getNewPassword().equals(resetRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        String token = request.getHeader("Authorization").substring(7);
        UUID userId = UUID.fromString(jwtService.extractUserId(token));

        String purpose = jwtService.extractClaim(token, claims -> claims.get("purpose", String.class));
        if (!"PASSWORD_RESET".equals(purpose)) {
            throw new RuntimeException("Invalid token type");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(resetRequest.getNewPassword()));
        userRepository.save(user);

        return "Password reset successfully";
    }
}

