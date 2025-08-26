package com.example.FruitApp.controller;

import com.example.FruitApp.dto.FruitsDto;
import com.example.FruitApp.model.Fruits;
import com.example.FruitApp.service.FruitsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/fruits")
@RequiredArgsConstructor
public class FruitsController {

    private final FruitsService fruitService;

    @GetMapping("/search")
    public List<Fruits> searchFruits(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return fruitService.searchFruits(keyword, page, size).getContent();
    }

    @GetMapping
    public List<Fruits> getAllFruits() {
        return fruitService.getAllFruits();
    }

    @GetMapping("/{id}")
    public Object getFruitsById(@PathVariable UUID id) {
        return fruitService.getFruitsById(id);
    }

    @PostMapping
    public Object SaveFruits (@Valid @RequestBody FruitsDto fruitsDto) {
        return fruitService.saveFruits(fruitsDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateFruits(@PathVariable UUID id, @Valid @RequestBody FruitsDto updatedFruit) {
        return fruitService.updateFruitsById(id, updatedFruit);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchFruits(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return fruitService.patchFruitsById(id, updates);
    }

    @DeleteMapping("/{id}")
    public String deleteFruitsById(@PathVariable UUID id) {
        return fruitService.deleteFruitsById(id);
    }

}
