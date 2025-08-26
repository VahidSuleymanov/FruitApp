package com.example.FruitApp.service;

import com.example.FruitApp.dto.KataqoriyaDto;
import com.example.FruitApp.model.Kataqoriyalar;
import com.example.FruitApp.model.Statuses;
import com.example.FruitApp.repository.KataqoriyaRepository;
import com.example.FruitApp.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KataqoriyaService {

    private final KataqoriyaRepository kataqoriyaRepository;

    private final StatusRepository statusRepository;

    public List<Kataqoriyalar> getAllCategory() {
        return kataqoriyaRepository.findAll();
    }

    public Object saveCategory(KataqoriyaDto kataqoriyaDto) {
        Optional<Kataqoriyalar> existingCategory = kataqoriyaRepository.findByName(kataqoriyaDto.getName());
        if (existingCategory.isPresent()) {
            return "Bu kataqoriya artiq sistemde var!";
        }

        Optional<Kataqoriyalar> existingCategoryByDescription = kataqoriyaRepository.findByDescription(kataqoriyaDto.getDescription());
        if (existingCategoryByDescription.isPresent()) {
            return "Bu Description artiq sistemde var!";
        }

        Statuses status = statusRepository.findById(kataqoriyaDto.getStatusId())
                .orElseThrow(() -> new RuntimeException("Status tapılmadı"));

        Kataqoriyalar kataqoriya = Kataqoriyalar.builder()
                .name(kataqoriyaDto.getName())
                .description(kataqoriyaDto.getDescription())
                .statusId(status)
                .build();

        kataqoriyaRepository.save(kataqoriya);

        return "Kataqoriya ugurla sisteme yazildi!";
    }

    public Object getCategoryById(UUID id) {
        Optional<Kataqoriyalar> optionalCategory = kataqoriyaRepository.findById(id);

        if (optionalCategory.isPresent()) {
            return ResponseEntity.ok(optionalCategory.get());
        } else {
            return ResponseEntity.ok("Kataqoriya tapılmadı!: ID = " + id);
        }
    }

    public ResponseEntity<String> updateCategoryById(UUID id, KataqoriyaDto updatedKataqoriya) {
        Optional<Kataqoriyalar> optionalCategory = kataqoriyaRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Kataqoriyalar kataqoriya = optionalCategory.get();

            Optional<Kataqoriyalar> optionalCategoryByName = kataqoriyaRepository.findByName(updatedKataqoriya.getName());
            if (optionalCategoryByName.isPresent() && !optionalCategoryByName.get().getId().equals(kataqoriya.getId())) {
                return ResponseEntity.ok("Bu kataqoriya artıq sistemdə var!");
            }
            kataqoriya.setName(updatedKataqoriya.getName());

            Optional<Kataqoriyalar> optionalCategoryByDescription = kataqoriyaRepository.findByDescription(updatedKataqoriya.getDescription());
            if (optionalCategoryByDescription.isPresent() && !optionalCategoryByDescription.get().getId().equals(kataqoriya.getId())) {
                return ResponseEntity.ok("Bu description artıq sistemdə var!");
            }
            kataqoriya.setDescription(updatedKataqoriya.getDescription());

            Optional<Statuses> statusOptional = statusRepository.findById(updatedKataqoriya.getStatusId());
            if (statusOptional.isEmpty()) {
                return ResponseEntity.ok("Belə bir status mövcud deyil!");
            }
            kataqoriya.setStatusId(statusOptional.get());

            kataqoriyaRepository.save(kataqoriya);
            return ResponseEntity.ok("Kataqoriya uğurla yeniləndi!");
        } else {
            return ResponseEntity.ok("Kataqoriya tapılmadı!");
        }
    }

    public ResponseEntity<String> patchCategoryById(UUID id, Map<String, Object> updates) {
        Optional<Kataqoriyalar> optionalCategory = kataqoriyaRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            return ResponseEntity.ok("Kataqoriya tapılmadı!");
        }

        Kataqoriyalar kataqoriya = optionalCategory.get();
        boolean updated = false;

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            switch (key) {
                case "name" -> {
                    if (value == null || !(value instanceof String) || ((String) value).isBlank()) {
                        break;
                    }
                    String newName = (String) value;
                    Optional<Kataqoriyalar> nameOwner = kataqoriyaRepository.findByName(newName);
                    if (nameOwner.isPresent() && !nameOwner.get().getId().equals(kataqoriya.getId())) {
                        return ResponseEntity.badRequest().body("Bu kataqoriya artıq sistemdə mövcuddur!");
                    }
                    if (!newName.equals(kataqoriya.getName())) {
                        kataqoriya.setName(newName);
                        updated = true;
                    }
                }

                case "description" -> {
                    if (value == null || !(value instanceof String) || ((String) value).isBlank()) {
                        break;
                    }
                    String newDescription = (String) value;
                    Optional<Kataqoriyalar> descriptionOwner = kataqoriyaRepository.findByDescription(newDescription);
                    if (descriptionOwner.isPresent() && !descriptionOwner.get().getId().equals(kataqoriya.getId())) {
                        return ResponseEntity.badRequest().body("Bu description artıq sistemdə mövcuddur!");
                    }
                    if (!newDescription.equals(kataqoriya.getDescription())) {
                        kataqoriya.setDescription(newDescription);
                        updated = true;
                    }
                }

                case "statusId" -> {
                    if (value == null || value.toString().isBlank()) {
                        break;
                    }
                    try {
                        UUID statusId = UUID.fromString(value.toString());
                        Optional<Statuses> optionalStatus = statusRepository.findById(statusId);
                        if (optionalStatus.isEmpty()) {
                            return ResponseEntity.badRequest().body("Belə bir status mövcud deyil!");
                        }
                        if (!optionalStatus.get().equals(kataqoriya.getStatusId())) {
                            kataqoriya.setStatusId(optionalStatus.get());
                            updated = true;
                        }
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("Status ID düzgün formatda deyil!");
                    }
                }

                default -> {
                    return ResponseEntity.badRequest().body("Naməlum sahə: " + key);
                }
            }
        }

        if (updated) {
            kataqoriyaRepository.save(kataqoriya);
            return ResponseEntity.ok("Kataqoriya məlumatları qismən yeniləndi!");
        } else {
            return ResponseEntity.ok("Heç bir məlumat yenilənmədi.");
        }
    }



    public String deleteCategoryById(UUID id) {
        if (!kataqoriyaRepository.existsById(id)) {
            return "Bu ID: " + id + " uzre kataqoriya tapilmadi!";
        }

        kataqoriyaRepository.deleteById(id);
        return "Kataqoriya silindi!";
    }

}
