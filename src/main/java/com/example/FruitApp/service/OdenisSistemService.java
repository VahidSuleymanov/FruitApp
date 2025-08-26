package com.example.FruitApp.service;

import com.example.FruitApp.dto.OdenisSistemleriDto;
import com.example.FruitApp.model.OdenisSistemleri;
import com.example.FruitApp.model.Statuses;
import com.example.FruitApp.repository.OdenisSistemRepository;
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
public class OdenisSistemService {

    private final OdenisSistemRepository odenisSistemRepository;

    private final StatusRepository statusRepository;


    public List<OdenisSistemleri> getAllSistems() {
        return odenisSistemRepository.findAll();
    }

    public Object saveSistems(OdenisSistemleriDto sistemDto) {
        Optional<OdenisSistemleri> existingSistem = odenisSistemRepository.findByName(sistemDto.getName());
        if (existingSistem.isPresent()) {
            return "Bu odenis sistemi artiq sistemde var!";
        }

        Optional<OdenisSistemleri> existingSistemByDescription = odenisSistemRepository.findByDescription(sistemDto.getDescription());
        if (existingSistemByDescription.isPresent()) {
            return "Bu Description artiq sistemde var!";
        }

        Optional<OdenisSistemleri> existingSistemByIcon = odenisSistemRepository.findByIcon(sistemDto.getIcon());
        if (existingSistemByIcon.isPresent()) {
            return "Bu icon artiq sistemde var!";
        }


        Statuses statuses = statusRepository.findById(sistemDto.getStatusId())
                .orElseThrow(() -> new RuntimeException("Status tapılmadı"));

        OdenisSistemleri sistem = OdenisSistemleri.builder()
                .name(sistemDto.getName())
                .description(sistemDto.getDescription())
                .icon(sistemDto.getIcon())
                .statusId(statuses)
                .build();

        odenisSistemRepository.save(sistem);

        return "Odenis sistemi ugurla sisteme yazildi!";
    }

    public Object getSistemById(UUID id) {
        Optional<OdenisSistemleri> optionalSistem = odenisSistemRepository.findById(id);

        if (optionalSistem.isPresent()) {
            return ResponseEntity.ok(optionalSistem.get());
        } else {
            return ResponseEntity.ok("Odenis sistemi tapılmadı!: ID = " + id);
        }
    }

    public ResponseEntity<String> updateSistemById(UUID id, OdenisSistemleriDto updatedSistem) {
        Optional<OdenisSistemleri> optionalSistem = odenisSistemRepository.findById(id);
        if (optionalSistem.isPresent()) {
            OdenisSistemleri sistem = optionalSistem.get();

            Optional<OdenisSistemleri> optionalSistemByName = odenisSistemRepository.findByName(updatedSistem.getName());
            if (optionalSistemByName.isPresent() && !optionalSistemByName.get().getId().equals(sistem.getId())) {
                return ResponseEntity.ok("Bu odenis sistemi artıq sistemdə var!");
            }
            sistem.setName(updatedSistem.getName());

            Optional<OdenisSistemleri> optionalSistemByDescription = odenisSistemRepository.findByDescription(updatedSistem.getDescription());
            if (optionalSistemByDescription.isPresent() && !optionalSistemByDescription.get().getId().equals(sistem.getId())) {
                return ResponseEntity.ok("Bu description artıq sistemdə var!");
            }
            sistem.setDescription(updatedSistem.getDescription());

            Optional<OdenisSistemleri> optionalSistemByIcon = odenisSistemRepository.findByIcon(updatedSistem.getIcon());
            if (optionalSistemByIcon.isPresent() && !optionalSistemByIcon.get().getId().equals(sistem.getId())) {
                return ResponseEntity.ok("Bu icon artıq sistemdə var!");
            }
            sistem.setIcon(updatedSistem.getIcon());

            Optional<Statuses> statusOptional = statusRepository.findById(updatedSistem.getStatusId());
            if (statusOptional.isEmpty()) {
                return ResponseEntity.ok("Belə bir status mövcud deyil!");
            }
            sistem.setStatusId(statusOptional.get());

            odenisSistemRepository.save(sistem);
            return ResponseEntity.ok("Odenis sistemi uğurla yeniləndi!");
        } else {
            return ResponseEntity.ok("Odenis sistemi tapılmadı!");
        }
    }

    public ResponseEntity<String> patchSistemById(UUID id, Map<String, Object> updates) {
        Optional<OdenisSistemleri> optionalSistem = odenisSistemRepository.findById(id);
        if (optionalSistem.isEmpty()) {
            return ResponseEntity.ok("Odenis sistemi tapılmadı!");
        }

        OdenisSistemleri sistem = optionalSistem.get();
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
                    Optional<OdenisSistemleri> nameOwner = odenisSistemRepository.findByName(newName);
                    if (nameOwner.isPresent() && !nameOwner.get().getId().equals(sistem.getId())) {
                        return ResponseEntity.badRequest().body("Bu odenis sistemi artıq sistemdə mövcuddur!");
                    }
                    if (!newName.equals(sistem.getName())) {
                        sistem.setName(newName);
                        updated = true;
                    }
                }

                case "description" -> {
                    if (value == null || !(value instanceof String) || ((String) value).isBlank()) {
                        break;
                    }
                    String newDescription = (String) value;
                    Optional<OdenisSistemleri> descriptionOwner = odenisSistemRepository.findByDescription(newDescription);
                    if (descriptionOwner.isPresent() && !descriptionOwner.get().getId().equals(sistem.getId())) {
                        return ResponseEntity.badRequest().body("Bu description artıq sistemdə mövcuddur!");
                    }
                    if (!newDescription.equals(sistem.getDescription())) {
                        sistem.setDescription(newDescription);
                        updated = true;
                    }
                }

                case "icon" -> {
                    if (value == null || !(value instanceof String) || ((String) value).isBlank()) {
                        break;
                    }
                    String newIcon = (String) value;
                    Optional<OdenisSistemleri> iconOwner = odenisSistemRepository.findByIcon(newIcon);
                    if (iconOwner.isPresent() && !iconOwner.get().getId().equals(sistem.getId())) {
                        return ResponseEntity.badRequest().body("Bu icon artıq sistemdə mövcuddur!");
                    }
                    if (!newIcon.equals(sistem.getIcon())) {
                        sistem.setIcon(newIcon);
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
                        if (!statusId.equals(sistem.getStatusId())) {
                            sistem.setStatusId(optionalStatus.get());
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
            odenisSistemRepository.save(sistem);
            return ResponseEntity.ok("Odenis sistemi məlumatları qismən yeniləndi!");
        } else {
            return ResponseEntity.ok("Heç bir məlumat yenilənmədi.");
        }
    }


    public String deleteSistemById(UUID id) {
        if (!odenisSistemRepository.existsById(id)) {
            return "Bu ID: " + id + " uzre odenis sistemi tapilmadi!";
        }

        odenisSistemRepository.deleteById(id);
        return "Odenis sistemi silindi!";
    }

}
