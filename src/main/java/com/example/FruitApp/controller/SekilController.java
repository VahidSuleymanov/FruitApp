package com.example.FruitApp.controller;

import com.example.FruitApp.dto.SekilDto;
import com.example.FruitApp.model.Sekiller;
import com.example.FruitApp.service.SekilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/sekil")
@RequiredArgsConstructor
public class SekilController {

    private final SekilService sekilService;

    @GetMapping
    public List<Sekiller> getAllPicture() {
        return sekilService.getAllPicture();
    }

    @GetMapping("/{id}")
    public Object getPictureById(@PathVariable UUID id) {
        return sekilService.getPictureById(id);
    }

    @PostMapping
    public Object savePicture(@Valid @RequestBody SekilDto sekilDto) {
        return sekilService.savePicture(sekilDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePicture(@PathVariable UUID id, @Valid @RequestBody SekilDto updatedPicture) {
        return sekilService.updatePictureById(id, updatedPicture);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchPicture(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return sekilService.patchPictureById(id, updates);
    }

    @DeleteMapping("/{id}")
    public String deletePictureById(@PathVariable UUID id) {
        return sekilService.deletePictureById(id);
    }

}
