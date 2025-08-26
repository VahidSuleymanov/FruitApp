package com.example.FruitApp.controller;

import com.example.FruitApp.model.Sebet;
import com.example.FruitApp.service.JwtService;
import com.example.FruitApp.service.SebetService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/sebet")
@RequiredArgsConstructor
public class SebetController {

    private final SebetService sebetService;

    private final JwtService jwtService;


    @GetMapping
    public List<Sebet> getUserBasket(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        String token = request.getHeader("Authorization").substring(7);
        return sebetService.getUserBasket(token, page, size).getContent();
    }

    @GetMapping("/all")
    public List<Sebet> getUserSebet(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        UUID userId = UUID.fromString(jwtService.extractUserId(token));
        return sebetService.getAllSebet(userId);
    }

    @GetMapping("/count")
    public ResponseEntity<?> getBasket(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        int productCount = sebetService.getUserBasketCount(token);
        Map<String, Integer> response = new HashMap<>();
        response.put("count", productCount);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{fruitId}")
    public ResponseEntity<Object> saveFruitToSebet(@PathVariable UUID fruitId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return ResponseEntity.ok(sebetService.addToSebet(token, fruitId));
    }

    @PatchMapping("/increase/{sebetId}")
    public ResponseEntity<Sebet> increaseSebetCount(@PathVariable UUID sebetId) {
        Sebet increaseSebet = sebetService.increaseProductCount(sebetId);
        return ResponseEntity.ok(increaseSebet);
    }

    @PatchMapping("/decrease/{sebetId}")
    public ResponseEntity<Sebet> decreaseSebetCount(@PathVariable UUID sebetId) {
        Sebet decreaseSebet = sebetService.decreaseProductCount(sebetId);
        return ResponseEntity.ok(decreaseSebet);
    }

    @GetMapping("/totalPrice")
    public Map<String, Double> getUserBasketTotalPrice(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        UUID userId = UUID.fromString(jwtService.extractUserId(token));
        return sebetService.getUserBasketTotalPrice(userId);
    }

    @GetMapping("/checkout")
    public ResponseEntity<List<String>> validateBasket(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userIdStr = jwtService.extractUserId(token);
        UUID userId = UUID.fromString(userIdStr);
        List<String> result = sebetService.validateUserBasket(userId);
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{id}")
    public String deleteSebetFruitsById(@PathVariable UUID id) {
        return sebetService.deleteSebetFruitsById(id);
    }

}
