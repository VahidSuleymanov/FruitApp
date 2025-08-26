package com.example.FruitApp.controller;

import com.example.FruitApp.dto.forgotPasswordDto.ForgotPasswordRequestDto;
import com.example.FruitApp.dto.forgotPasswordDto.ForgotPasswordTokenResponseDto;
import com.example.FruitApp.dto.forgotPasswordDto.OtpVerifyRequestDto;
import com.example.FruitApp.dto.forgotPasswordDto.ResetPasswordRequestDto;
import com.example.FruitApp.service.ForgotPasswordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/forgot-password")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    // 1️⃣ Request OTP + OTP_VERIFY token
    @PostMapping("/request")
    public ResponseEntity<ForgotPasswordTokenResponseDto> requestReset(@Valid @RequestBody ForgotPasswordRequestDto request) {
        return ResponseEntity.ok(forgotPasswordService.requestReset(request.getEmail()));
    }

    // 2️⃣ Verify OTP və PASSWORD_RESET token qaytar
    @PostMapping("/verify-otp")
    public ResponseEntity<ForgotPasswordTokenResponseDto> verifyOtp(HttpServletRequest request,
                                                                    @Valid @RequestBody OtpVerifyRequestDto otpRequest) {
        return ResponseEntity.ok(forgotPasswordService.verifyOtp(request, otpRequest));
    }

    // 3️⃣ Reset password (yalnız PASSWORD_RESET token ilə)
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(HttpServletRequest request,
                                                @Valid @RequestBody ResetPasswordRequestDto resetRequest) {
        return ResponseEntity.ok(forgotPasswordService.resetPassword(request, resetRequest));
    }
}
