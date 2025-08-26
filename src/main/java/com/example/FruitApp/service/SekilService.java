package com.example.FruitApp.service;

import com.example.FruitApp.dto.SekilDto;
import com.example.FruitApp.model.Fruits;
import com.example.FruitApp.model.Sekiller;
import com.example.FruitApp.model.Statuses;
import com.example.FruitApp.repository.FruitsRepository;
import com.example.FruitApp.repository.SekilRepository;
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
public class SekilService {

    private final SekilRepository sekilRepository;

    private final StatusRepository statusRepository;

    private final FruitsRepository fruitsRepository;

    public List<Sekiller> getAllPicture() {
        return sekilRepository.findAll();
    }

    public Object savePicture(SekilDto sekilDto) {
        Optional<Sekiller> existingPicture = sekilRepository.findByName(sekilDto.getName());
        if (existingPicture.isPresent()) {
            return "Bu sekil artiq sistemde var!";
        }

        Fruits fruits = fruitsRepository.findById(sekilDto.getFruitsId())
                .orElseThrow(() -> new RuntimeException("Mehsul tapılmadı"));
        Statuses status = statusRepository.findById(sekilDto.getStatusId())
                .orElseThrow(() -> new RuntimeException("Status tapılmadı"));

        Sekiller sekil = Sekiller.builder()
                .name(sekilDto.getName())
                .fruitsId(fruits)
                .statusId(status)
                .build();

        sekilRepository.save(sekil);

        return "Sekil ugurla sisteme yazildi!";
    }

    public Object getPictureById(UUID id) {
        Optional<Sekiller> optionalPicture = sekilRepository.findById(id);

        if (optionalPicture.isPresent()) {
            return ResponseEntity.ok(optionalPicture.get());
        } else {
            return ResponseEntity.ok("Sekil tapılmadı!: ID = " + id);
        }
    }

    public ResponseEntity<String> updatePictureById(UUID id, SekilDto updatedSekil) {
        Optional<Sekiller> optionalPicture = sekilRepository.findById(id);
        if (optionalPicture.isPresent()) {
            Sekiller sekil = optionalPicture.get();

            Optional<Sekiller> optionalPictureByName = sekilRepository.findByName(updatedSekil.getName());
            if (optionalPictureByName.isPresent() && !optionalPictureByName.get().getId().equals(sekil.getId())) {
                return ResponseEntity.ok("Bu sekil artıq sistemdə var!");
            }
            sekil.setName(updatedSekil.getName());

            Optional<Fruits> fruitsOptional = fruitsRepository.findById(updatedSekil.getFruitsId());
            if (fruitsOptional.isEmpty()) {
                return ResponseEntity.ok("Belə bir mehsul mövcud deyil!");
            }
            sekil.setFruitsId(fruitsOptional.get());

            Optional<Statuses> statusOptional = statusRepository.findById(updatedSekil.getStatusId());
            if (statusOptional.isEmpty()) {
                return ResponseEntity.ok("Belə bir status mövcud deyil!");
            }
            sekil.setStatusId(statusOptional.get());

            sekilRepository.save(sekil);
            return ResponseEntity.ok("Sekil uğurla yeniləndi!");
        } else {
            return ResponseEntity.ok("Sekil tapılmadı!");
        }
    }

    public ResponseEntity<String> patchPictureById(UUID id, Map<String, Object> updates) {
        Optional<Sekiller> optionalPicture = sekilRepository.findById(id);
        if (optionalPicture.isEmpty()) {
            return ResponseEntity.ok("Sekil tapılmadı!");
        }

        Sekiller sekil = optionalPicture.get();
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
                    Optional<Sekiller> nameOwner = sekilRepository.findByName(newName);
                    if (nameOwner.isPresent() && !nameOwner.get().getId().equals(sekil.getId())) {
                        return ResponseEntity.badRequest().body("Bu sekil artıq sistemdə mövcuddur!");
                    }
                    if (!newName.equals(sekil.getName())) {
                        sekil.setName(newName);
                        updated = true;
                    }
                }

                case "fruitsId" -> {
                    if (value == null || value.toString().isBlank()) {
                        break;
                    }
                    try {
                        UUID fruitsId = UUID.fromString(value.toString());
                        Optional<Fruits> optionalFruits = fruitsRepository.findById(fruitsId);
                        if (optionalFruits.isEmpty()) {
                            return ResponseEntity.badRequest().body("Belə bir mehsul mövcud deyil!");
                        }
                        if (!optionalFruits.get().equals(sekil.getFruitsId())) {
                            sekil.setFruitsId(optionalFruits.get());
                            updated = true;
                        }
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("Mehsul ID düzgün formatda deyil!");
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
                        if (!optionalStatus.get().equals(sekil.getStatusId())) {
                            sekil.setStatusId(optionalStatus.get());
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
            sekilRepository.save(sekil);
            return ResponseEntity.ok("Sekil məlumatları qismən yeniləndi!");
        } else {
            return ResponseEntity.ok("Heç bir məlumat yenilənmədi.");
        }
    }



    public String deletePictureById(UUID id) {
        if (!sekilRepository.existsById(id)) {
            return "Bu ID: " + id + " uzre sekil tapilmadi!";
        }

        sekilRepository.deleteById(id);
        return "Sekil silindi!";
    }

}
