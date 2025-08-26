package com.example.FruitApp.controller;

import com.example.FruitApp.dto.OdenisSistemleriDto;
import com.example.FruitApp.model.OdenisSistemleri;
import com.example.FruitApp.service.OdenisSistemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/sistemler")
@RequiredArgsConstructor
public class OdenisSistemleriController {

    private final OdenisSistemService odenisService;

    @GetMapping
    public List<OdenisSistemleri> getAllSistems() {
        return odenisService.getAllSistems();
    }

    @GetMapping("/{id}")
    public Object getSistemById(@PathVariable UUID id) {
        return odenisService.getSistemById(id);
    }

    @PostMapping
    public Object saveSistems(@Valid @RequestBody OdenisSistemleriDto sistemDto) {
        return odenisService.saveSistems(sistemDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSistems(@PathVariable UUID id, @Valid @RequestBody OdenisSistemleriDto updatedSistems) {
        return odenisService.updateSistemById(id, updatedSistems);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchSistems(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return odenisService.patchSistemById(id, updates);
    }

    @DeleteMapping("/{id}")
    public String deleteSistemById(@PathVariable UUID id) {
        return odenisService.deleteSistemById(id);
    }

}
