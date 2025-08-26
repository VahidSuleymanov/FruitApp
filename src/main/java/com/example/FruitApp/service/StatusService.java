package com.example.FruitApp.service;

import com.example.FruitApp.dto.StatusesDto;
import com.example.FruitApp.model.Statuses;
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
public class StatusService {

    private final StatusRepository statusRepository;

    public List<Statuses> getAllStatuses() {
        return statusRepository.findAll();
    }

    public Object saveStatuses(StatusesDto statusDto) {
        Optional<Statuses> existingStatus = statusRepository.findByName(statusDto.getName());
        if (existingStatus.isPresent()) {
            return "Bu status artiq sistemde var!";
        }

        Optional<Statuses> existingStatusByDescription = statusRepository.findByDescription(statusDto.getDescription());
        if (existingStatusByDescription.isPresent()) {
            return "Bu Description artiq sistemde var!";
        }

        Statuses statuses = statusRepository.findById(statusDto.getStatusId())
                .orElseThrow(() -> new RuntimeException("Status tapılmadı"));

        Statuses status = Statuses.builder()
                .name(statusDto.getName().toUpperCase())
                .description(statusDto.getDescription())
                .statusId(statuses.getId())
                .build();

        statusRepository.save(status);

        return "Status ugurla sisteme yazildi!";
    }

    public Object getStatusById(UUID id) {
        Optional<Statuses> optionalStatuses = statusRepository.findById(id);

        if (optionalStatuses.isPresent()) {
            return ResponseEntity.ok(optionalStatuses.get());
        } else {
            return ResponseEntity.ok("Status tapılmadı!: ID = " + id);
        }
    }

    public ResponseEntity<String> updateStatusById(UUID id, StatusesDto updatedStatus) {
        Optional<Statuses> optionalStatuses = statusRepository.findById(id);
        if (optionalStatuses.isPresent()) {
            Statuses status = optionalStatuses.get();

            Optional<Statuses> optionalStatusByName = statusRepository.findByName(updatedStatus.getName().toUpperCase());
            if (optionalStatusByName.isPresent() && !optionalStatusByName.get().getId().equals(status.getId())) {
                return ResponseEntity.ok("Bu status artıq sistemdə var!");
            }
            status.setName(updatedStatus.getName().toUpperCase());

            Optional<Statuses> optionalStatusByDescription = statusRepository.findByDescription(updatedStatus.getDescription());
            if (optionalStatusByDescription.isPresent() && !optionalStatusByDescription.get().getId().equals(status.getId())) {
                return ResponseEntity.ok("Bu description artıq sistemdə var!");
            }
            status.setDescription(updatedStatus.getDescription());

            Optional<Statuses> statusOptional = statusRepository.findById(updatedStatus.getStatusId());
            if (statusOptional.isEmpty()) {
                return ResponseEntity.ok("Belə bir status mövcud deyil!");
            }
            status.setStatusId(statusOptional.get().getId());

            statusRepository.save(status);
            return ResponseEntity.ok("Status uğurla yeniləndi!");
        } else {
            return ResponseEntity.ok("Status tapılmadı!");
        }
    }

    public ResponseEntity<String> patchStatusById(UUID id, Map<String, Object> updates) {
        Optional<Statuses> optionalStatuses = statusRepository.findById(id);
        if (optionalStatuses.isEmpty()) {
            return ResponseEntity.ok("Status tapılmadı!");
        }

        Statuses status = optionalStatuses.get();
        boolean updated = false;

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            switch (key) {
                case "name" -> {
                    if (value == null || !(value instanceof String) || ((String) value).isBlank()) {
                        break;
                    }
                    String newName = ((String) value).toUpperCase();
                    Optional<Statuses> nameOwner = statusRepository.findByName(newName);
                    if (nameOwner.isPresent() && !nameOwner.get().getId().equals(status.getId())) {
                        return ResponseEntity.badRequest().body("Bu status artıq sistemdə mövcuddur!");
                    }
                    if (!newName.equals(status.getName())) {
                        status.setName(newName);
                        updated = true;
                    }
                }

                case "description" -> {
                    if (value == null || !(value instanceof String) || ((String) value).isBlank()) {
                        break;
                    }
                    String newDescription = (String) value;
                    Optional<Statuses> descriptionOwner = statusRepository.findByDescription(newDescription);
                    if (descriptionOwner.isPresent() && !descriptionOwner.get().getId().equals(status.getId())) {
                        return ResponseEntity.badRequest().body("Bu description artıq sistemdə mövcuddur!");
                    }
                    if (!newDescription.equals(status.getDescription())) {
                        status.setDescription(newDescription);
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
                        if (!statusId.equals(status.getStatusId())) {
                            status.setStatusId(statusId);
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
            statusRepository.save(status);
            return ResponseEntity.ok("Status məlumatları qismən yeniləndi!");
        } else {
            return ResponseEntity.ok("Heç bir məlumat yenilənmədi.");
        }
    }


    public String deleteStatusById(UUID id) {
        if (!statusRepository.existsById(id)) {
            return "Bu ID: " + id + " uzre status tapilmadi!";
        }

        statusRepository.deleteById(id);
        return "Status silindi!";
    }

}
