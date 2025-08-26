package com.example.FruitApp.controller;

import com.example.FruitApp.dto.KataqoriyaDto;
import com.example.FruitApp.model.Kataqoriyalar;
import com.example.FruitApp.service.KataqoriyaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/kataqoriya")
@RequiredArgsConstructor
public class KataqoriyaController {

    private final KataqoriyaService kataqoriyaService;

    @GetMapping
    public List<Kataqoriyalar> getAllCategory() {
        return kataqoriyaService.getAllCategory();
    }

    @GetMapping("/{id}")
    public Object getCategoryById(@PathVariable UUID id) {
        return kataqoriyaService.getCategoryById(id);
    }

    @PostMapping
    public Object saveCategory(@Valid @RequestBody KataqoriyaDto kataqoriyaDto) {
        return kataqoriyaService.saveCategory(kataqoriyaDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable UUID id, @Valid @RequestBody KataqoriyaDto updatedCategory) {
        return kataqoriyaService.updateCategoryById(id, updatedCategory);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchCategory(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return kataqoriyaService.patchCategoryById(id, updates);
    }

    @DeleteMapping("/{id}")
    public String deleteCategoryById(@PathVariable UUID id) {
        return kataqoriyaService.deleteCategoryById(id);
    }

}
