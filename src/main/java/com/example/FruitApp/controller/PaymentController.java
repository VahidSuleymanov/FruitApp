package com.example.FruitApp.controller;

import com.example.FruitApp.dto.UserCartsDto;
import com.example.FruitApp.service.JwtService;
import com.example.FruitApp.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<Map<String, String>> processPayment(@Valid @RequestBody UserCartsDto dto,
                                                              HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            UUID userId = UUID.fromString(jwtService.extractUserId(token));

            String message = paymentService.processPayment(dto, userId);

            Map<String, String> response = new HashMap<>();
            response.put("message", message);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
