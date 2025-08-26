package com.example.FruitApp.controller;

import com.example.FruitApp.dto.OdenisKartlariDto;
import com.example.FruitApp.model.OdenisKartlari;
import com.example.FruitApp.service.OdenisKartlariService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/bankkartlari")
@RequiredArgsConstructor
public class OdenisKartlariController {

    private final OdenisKartlariService cartsService;

    @GetMapping
    public List<OdenisKartlari> getAllCarts() {
        return cartsService.getAllCarts();
    }

//    @GetMapping("/{id}")
//    public Object getSistemById(@PathVariable UUID id) {
//        return odenisService.getSistemById(id);
//    }

    @PostMapping
    public Object saveCarts(@Valid @RequestBody OdenisKartlariDto cartDto) {
        return cartsService.saveCarts(cartDto);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<String> updateSistems(@PathVariable UUID id, @Valid @RequestBody OdenisSistemleriDto updatedSistems) {
//        return odenisService.updateSistemById(id, updatedSistems);
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<String> patchSistems(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
//        return odenisService.patchSistemById(id, updates);
//    }

    @DeleteMapping("/{id}")
    public String deleteCartsById(@PathVariable UUID id) {
        return cartsService.deleteCartById(id);
    }

}
