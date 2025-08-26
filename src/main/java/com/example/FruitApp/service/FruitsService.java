package com.example.FruitApp.service;

import com.example.FruitApp.dto.FruitsDto;
import com.example.FruitApp.model.*;
import com.example.FruitApp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FruitsService {

    private final FruitsRepository fruitRepository;

    private final KataqoriyaRepository kataqoriyaRepository;

    private final ValyutaRepository valyutaRepository;

    private final KemiyyetRepository kemiyyetRepository;

    private final StatusRepository statusRepository;


    public Page<Fruits> searchFruits(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return fruitRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    public List<Fruits> getAllFruits() {
        return fruitRepository.findAll();
    }

    public Object saveFruits(FruitsDto fruitsDto) {
        Optional<Fruits> existingFruits = fruitRepository.findByName(fruitsDto.getName());
        if (existingFruits.isPresent()) {
            return "Bu meyve artiq sistemde var!";
        }

        Kataqoriyalar category = kataqoriyaRepository.findById(fruitsDto.getKataqoriyaId())
                .orElseThrow(() -> new RuntimeException("Kategoriya tapılmadı"));
        Valyutalar valyuta = valyutaRepository.findById(fruitsDto.getValyutaId())
                .orElseThrow(() -> new RuntimeException("Valyuta tapılmadı"));
        Kemiyyetler kemiyyet = kemiyyetRepository.findById(fruitsDto.getKemiyyetId())
                .orElseThrow(() -> new RuntimeException("Kemiyyet tapılmadı"));
        Statuses status = statusRepository.findById(fruitsDto.getStatusId())
                .orElseThrow(() -> new RuntimeException("Status tapılmadı"));

        Fruits fruits = Fruits.builder()
                .name(fruitsDto.getName())
                .qiymet(fruitsDto.getQiymet())
                .miqdar(fruitsDto.getMiqdar())
                .kataqoriyaId(category)
                .valyutaId(valyuta)
                .kemiyyetId(kemiyyet)
                .statusId(status)
                .build();

        fruitRepository.save(fruits);

        return "Meyve ugurla sisteme yazildi!";
    }

    public Object getFruitsById(UUID id) {
        Optional<Fruits> optionalFruits = fruitRepository.findById(id);

        if (optionalFruits.isPresent()) {
            return ResponseEntity.ok(optionalFruits.get());
        } else {
            return ResponseEntity.ok("Mehsul tapılmadı!: ID = " + id);
        }
    }

    public ResponseEntity<String> updateFruitsById(UUID id, FruitsDto updatedFruit) {
        Optional<Fruits> optionalFruits = fruitRepository.findById(id);
        if (optionalFruits.isPresent()) {
            Fruits fruit = optionalFruits.get();
            Optional<Fruits> optionalFruitsByName = fruitRepository.findByName(updatedFruit.getName());
            if (optionalFruitsByName.isPresent() && !optionalFruitsByName.get().getId().equals(fruit.getId())) {
                return ResponseEntity.ok("Bu meyvə artıq sistemdə mövcuddur!");
            }
            fruit.setName(updatedFruit.getName());
            fruit.setQiymet(updatedFruit.getQiymet());
            fruit.setMiqdar(updatedFruit.getMiqdar());

            Optional<Kataqoriyalar> kataqoriyaOptional = kataqoriyaRepository.findById(updatedFruit.getKataqoriyaId());
            if (kataqoriyaOptional.isEmpty()) {
                return ResponseEntity.ok("Belə bir kataqoriya mövcud deyil!");
            }
            fruit.setKataqoriyaId(kataqoriyaOptional.get());

            Optional<Kemiyyetler> kemiyyetOptional = kemiyyetRepository.findById(updatedFruit.getKemiyyetId());
            if (kemiyyetOptional.isEmpty()) {
                return ResponseEntity.ok("Belə bir kemiyyet mövcud deyil!");
            }
            fruit.setKemiyyetId(kemiyyetOptional.get());

            Optional<Valyutalar> valyutaOptional = valyutaRepository.findById(updatedFruit.getValyutaId());
            if (valyutaOptional.isEmpty()) {
                return ResponseEntity.ok("Belə bir valyuta mövcud deyil!");
            }
            fruit.setValyutaId(valyutaOptional.get());

            Optional<Statuses> statusOptional = statusRepository.findById(updatedFruit.getStatusId());
            if (statusOptional.isEmpty()) {
                return ResponseEntity.ok("Belə bir status mövcud deyil!");
            }
            fruit.setStatusId(statusOptional.get());

            fruitRepository.save(fruit);
            return ResponseEntity.ok("Mehsul uğurla yeniləndi!");
        } else {
            return ResponseEntity.ok("Mehsul tapılmadı!");
        }

    }

    public ResponseEntity<String> patchFruitsById(UUID id, Map<String, Object> updates) {
        Optional<Fruits> optionalFruits = fruitRepository.findById(id);
        if (optionalFruits.isEmpty()) {
            return ResponseEntity.ok("Məhsul tapılmadı!");
        }

        Fruits fruit = optionalFruits.get();
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
                    Optional<Fruits> nameOwner = fruitRepository.findByName(newName);
                    if (nameOwner.isPresent() && !nameOwner.get().getId().equals(fruit.getId())) {
                        return ResponseEntity.badRequest().body("Bu meyvə artıq sistemdə mövcuddur!");
                    }
                    if (!newName.equals(fruit.getName())) {
                        fruit.setName(newName);
                        updated = true;
                    }
                }

                case "qiymet" -> {
                    fruit.setQiymet(Double.parseDouble(value.toString()));
                    updated = true;
                }
                case "miqdar" -> {
                    fruit.setMiqdar(Integer.parseInt(value.toString()));
                    updated = true;
                }

                case "kataqoriyaId" -> {
                    if (value == null || value.toString().isBlank()) break;
                    try {
                        UUID kataqoriyaId = UUID.fromString(value.toString());
                        Optional<Kataqoriyalar> optionalKataqoriya = kataqoriyaRepository.findById(kataqoriyaId);
                        if (optionalKataqoriya.isEmpty()) {
                            return ResponseEntity.badRequest().body("Belə bir kataqoriya mövcud deyil!");
                        }
                        if (!optionalKataqoriya.get().equals(fruit.getKataqoriyaId())) {
                            fruit.setKataqoriyaId(optionalKataqoriya.get());
                            updated = true;
                        }
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("Kataqoriya ID düzgün formatda deyil!");
                    }
                }

                case "kemiyyetId" -> {
                    if (value == null || value.toString().isBlank()) break;
                    try {
                        UUID kemiyyetId = UUID.fromString(value.toString());
                        Optional<Kemiyyetler> optionalKemiyyet = kemiyyetRepository.findById(kemiyyetId);
                        if (optionalKemiyyet.isEmpty()) {
                            return ResponseEntity.badRequest().body("Belə bir kemiyyet mövcud deyil!");
                        }
                        if (!optionalKemiyyet.get().equals(fruit.getKemiyyetId())) {
                            fruit.setKemiyyetId(optionalKemiyyet.get());
                            updated = true;
                        }
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("Kemiyyet ID düzgün formatda deyil!");
                    }
                }

                case "valyutaId" -> {
                    if (value == null || value.toString().isBlank()) break;
                    try {
                        UUID valyutaId = UUID.fromString(value.toString());
                        Optional<Valyutalar> optionalValyuta = valyutaRepository.findById(valyutaId);
                        if (optionalValyuta.isEmpty()) {
                            return ResponseEntity.badRequest().body("Belə bir valyuta mövcud deyil!");
                        }
                        if (!optionalValyuta.get().equals(fruit.getValyutaId())) {
                            fruit.setValyutaId(optionalValyuta.get());
                            updated = true;
                        }
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("Valyuta ID düzgün formatda deyil!");
                    }
                }

                case "statusId" -> {
                    if (value == null || value.toString().isBlank()) break;
                    try {
                        UUID statusId = UUID.fromString(value.toString());
                        Optional<Statuses> optionalStatus = statusRepository.findById(statusId);
                        if (optionalStatus.isEmpty()) {
                            return ResponseEntity.badRequest().body("Belə bir status mövcud deyil!");
                        }
                        if (!optionalStatus.get().equals(fruit.getStatusId())) {
                            fruit.setStatusId(optionalStatus.get());
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
            fruitRepository.save(fruit);
            return ResponseEntity.ok("Məhsulun məlumatları qismən yeniləndi!");
        } else {
            return ResponseEntity.ok("Heç bir məlumat yenilənmədi.");
        }
    }


    public String deleteFruitsById(UUID id) {
        if (!fruitRepository.existsById(id)) {
            return "Bu ID: " + id + " uzre mehsul tapilmadi!";
        }

        fruitRepository.deleteById(id);
        return "Mehsul silindi!";
    }

}
