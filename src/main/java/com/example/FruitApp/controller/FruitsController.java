package com.example.FruitApp.controller;

import com.example.FruitApp.dto.FruitsDto;
import com.example.FruitApp.model.Fruits;
import com.example.FruitApp.service.FruitsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/fruits")
@RequiredArgsConstructor
public class FruitsController {

    private final FruitsService fruitService;

    @GetMapping
    public List<Fruits> getAllFruits() {
        return fruitService.getAllFruits();
    }

    @GetMapping("/{id}")
    public Object getFruitsById(@PathVariable UUID id) {
        return fruitService.getFruitsById(id);
    }

    @PostMapping
    public String SaveFruits (@RequestBody FruitsDto fruitsDto) {
        return fruitService.saveFruits(fruitsDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateFruits(@PathVariable UUID id, @RequestBody Fruits updatedFruit) {
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
