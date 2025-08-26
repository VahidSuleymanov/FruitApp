package com.example.FruitApp.controller;

import com.example.FruitApp.dto.StatusesDto;
import com.example.FruitApp.model.Statuses;
import com.example.FruitApp.service.StatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/status")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @GetMapping
    public List<Statuses> getAllStatuses() {
        return statusService.getAllStatuses();
    }

    @GetMapping("/{id}")
    public Object getStatusById(@PathVariable UUID id) {
        return statusService.getStatusById(id);
    }

    @PostMapping
    public Object saveStatus(@Valid @RequestBody StatusesDto statusDto) {
        return statusService.saveStatuses(statusDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable UUID id, @Valid @RequestBody StatusesDto updatedStatus) {
        return statusService.updateStatusById(id, updatedStatus);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchStatus(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return statusService.patchStatusById(id, updates);
    }

    @DeleteMapping("/{id}")
    public String deleteStatusById(@PathVariable UUID id) {
        return statusService.deleteStatusById(id);
    }

}
