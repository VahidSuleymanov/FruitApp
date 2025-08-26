package com.example.FruitApp.service;

import com.example.FruitApp.dto.KemiyyetDto;
import com.example.FruitApp.model.Kemiyyetler;
import com.example.FruitApp.model.Statuses;
import com.example.FruitApp.repository.KemiyyetRepository;
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
public class KemiyyetService {

    private final KemiyyetRepository kemiyyetRepository;

    private final StatusRepository statusRepository;

    public List<Kemiyyetler> getAllQuantity() {
        return kemiyyetRepository.findAll();
    }

    public Object saveQuantity(KemiyyetDto kemiyyetDto) {
        Optional<Kemiyyetler> existingQuantity = kemiyyetRepository.findByName(kemiyyetDto.getName());
        if (existingQuantity.isPresent()) {
            return "Bu kemiyyet artiq sistemde var!";
        }

        Optional<Kemiyyetler> existingQuantityByAbbv = kemiyyetRepository.findByAbbv(kemiyyetDto.getAbbv().toLowerCase());
        if (existingQuantityByAbbv.isPresent()) {
            return "Bu abreviatura artiq sistemde var!";
        }

        Statuses status = statusRepository.findById(kemiyyetDto.getStatusId())
                .orElseThrow(() -> new RuntimeException("Status tapılmadı"));

        Kemiyyetler kemiyyet = Kemiyyetler.builder()
                .name(kemiyyetDto.getName())
                .abbv(kemiyyetDto.getAbbv().toLowerCase())
                .statusId(status)
                .build();

        kemiyyetRepository.save(kemiyyet);

        return "Kemiyyet ugurla sisteme yazildi!";
    }

    public Object getQuantityById(UUID id) {
        Optional<Kemiyyetler> optionalQuantity = kemiyyetRepository.findById(id);

        if (optionalQuantity.isPresent()) {
            return ResponseEntity.ok(optionalQuantity.get());
        } else {
            return ResponseEntity.ok("Kemiyyet tapılmadı!: ID = " + id);
        }
    }

    public ResponseEntity<String> updateQuantityById(UUID id, KemiyyetDto updatedKemiyyet) {
        Optional<Kemiyyetler> optionalQuantity = kemiyyetRepository.findById(id);
        if (optionalQuantity.isPresent()) {
            Kemiyyetler kemiyyet = optionalQuantity.get();

            Optional<Kemiyyetler> optionalQuantityByName = kemiyyetRepository.findByName(updatedKemiyyet.getName());
            if (optionalQuantityByName.isPresent() && !optionalQuantityByName.get().getId().equals(kemiyyet.getId())) {
                return ResponseEntity.ok("Bu kemiyyet artıq sistemdə var!");
            }
            kemiyyet.setName(updatedKemiyyet.getName());

            Optional<Kemiyyetler> optionalQuantityByAbbv = kemiyyetRepository.findByAbbv(updatedKemiyyet.getAbbv().toLowerCase());
            if (optionalQuantityByAbbv.isPresent() && !optionalQuantityByAbbv.get().getId().equals(kemiyyet.getId())) {
                return ResponseEntity.ok("Bu abreviatura artıq sistemdə var!");
            }
            kemiyyet.setAbbv(updatedKemiyyet.getAbbv().toLowerCase());

            Optional<Statuses> statusOptional = statusRepository.findById(updatedKemiyyet.getStatusId());
            if (statusOptional.isEmpty()) {
                return ResponseEntity.ok("Belə bir status mövcud deyil!");
            }
            kemiyyet.setStatusId(statusOptional.get());

            kemiyyetRepository.save(kemiyyet);
            return ResponseEntity.ok("Kemiyyet uğurla yeniləndi!");
        } else {
            return ResponseEntity.ok("Kemiyyet tapılmadı!");
        }
    }

    public ResponseEntity<String> patchQuantityById(UUID id, Map<String, Object> updates) {
        Optional<Kemiyyetler> optionalQuantity = kemiyyetRepository.findById(id);
        if (optionalQuantity.isEmpty()) {
            return ResponseEntity.ok("Kemiyyet tapılmadı!");
        }

        Kemiyyetler kemiyyet = optionalQuantity.get();
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
                    Optional<Kemiyyetler> nameOwner = kemiyyetRepository.findByName(newName);
                    if (nameOwner.isPresent() && !nameOwner.get().getId().equals(kemiyyet.getId())) {
                        return ResponseEntity.badRequest().body("Bu kemiyyet artıq sistemdə mövcuddur!");
                    }
                    if (!newName.equals(kemiyyet.getName())) {
                        kemiyyet.setName(newName);
                        updated = true;
                    }
                }

                case "abbv" -> {
                    if (value == null || !(value instanceof String) || ((String) value).isBlank()) {
                        break;
                    }
                    String newAbbv = ((String) value).toLowerCase();
                    Optional<Kemiyyetler> abbvOwner = kemiyyetRepository.findByAbbv(newAbbv);
                    if (abbvOwner.isPresent() && !abbvOwner.get().getId().equals(kemiyyet.getId())) {
                        return ResponseEntity.badRequest().body("Bu abreviatura artıq sistemdə mövcuddur!");
                    }
                    if (!newAbbv.equals(kemiyyet.getAbbv())) {
                        kemiyyet.setAbbv(newAbbv);
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
                        if (!optionalStatus.get().equals(kemiyyet.getStatusId())) {
                            kemiyyet.setStatusId(optionalStatus.get());
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
            kemiyyetRepository.save(kemiyyet);
            return ResponseEntity.ok("Kemiyyet məlumatları qismən yeniləndi!");
        } else {
            return ResponseEntity.ok("Heç bir məlumat yenilənmədi.");
        }
    }



    public String deleteQuantityById(UUID id) {
        if (!kemiyyetRepository.existsById(id)) {
            return "Bu ID: " + id + " uzre kemiyyet tapilmadi!";
        }

        kemiyyetRepository.deleteById(id);
        return "Kemiyyet silindi!";
    }

}
