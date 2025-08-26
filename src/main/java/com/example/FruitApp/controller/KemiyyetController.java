package com.example.FruitApp.controller;

import com.example.FruitApp.dto.KemiyyetDto;
import com.example.FruitApp.model.Kemiyyetler;
import com.example.FruitApp.service.KemiyyetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/kemiyyet")
@RequiredArgsConstructor
public class KemiyyetController {

    private final KemiyyetService kemiyyetService;

    @GetMapping
    public List<Kemiyyetler> getAllQuantity() {
        return kemiyyetService.getAllQuantity();
    }

    @GetMapping("/{id}")
    public Object getQuantityById(@PathVariable UUID id) {
        return kemiyyetService.getQuantityById(id);
    }

    @PostMapping
    public Object saveQuantity(@Valid @RequestBody KemiyyetDto kemiyyetDto) {
        return kemiyyetService.saveQuantity(kemiyyetDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateQuantity(@PathVariable UUID id, @Valid @RequestBody KemiyyetDto updatedQuantity) {
        return kemiyyetService.updateQuantityById(id, updatedQuantity);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchQuantity(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return kemiyyetService.patchQuantityById(id, updates);
    }

    @DeleteMapping("/{id}")
    public String deleteQuantityById(@PathVariable UUID id) {
        return kemiyyetService.deleteQuantityById(id);
    }

}
