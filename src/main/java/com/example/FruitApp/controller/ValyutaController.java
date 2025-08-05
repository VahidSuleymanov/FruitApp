package com.example.FruitApp.controller;

import com.example.FruitApp.dto.ValyutaDto;
import com.example.FruitApp.model.Valyutalar;
import com.example.FruitApp.service.ValyutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/valyuta")
@RequiredArgsConstructor
public class ValyutaController {

    private final ValyutaService valyutaService;

    @GetMapping
    public List<Valyutalar> getAllFruits() {
        return valyutaService.getAllCurrency();
    }

    @GetMapping("/{id}")
    public Object getCurrencyById(@PathVariable UUID id) {
        return valyutaService.getCurrencyById(id);
    }

    @PostMapping
    public String saveCurrency(@RequestBody ValyutaDto valyutaDto) {
        return valyutaService.saveCurrency(valyutaDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCurrency(@PathVariable UUID id, @RequestBody Valyutalar updatedCurrency) {
        return valyutaService.updateCurrencyById(id, updatedCurrency);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchCurrency(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return valyutaService.patchCurrencyById(id, updates);
    }

    @DeleteMapping("/{id}")
    public String deleteCurrencyById(@PathVariable UUID id) {
        return valyutaService.deleteCurrencyById(id);
    }

}
