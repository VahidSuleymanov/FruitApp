package com.example.FruitApp.service;


import com.example.FruitApp.dto.FruitsDto;
import com.example.FruitApp.model.Fruits;
import com.example.FruitApp.repository.FruitsRepository;
import lombok.RequiredArgsConstructor;
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

    public List<Fruits> getAllFruits() {
        return fruitRepository.findAll();
    }

    public String saveFruits(FruitsDto fruitsDto) {
        Optional<Fruits> existingUser = fruitRepository.findByName(fruitsDto.getName());
        if (existingUser.isPresent()) {
            return "Bu meyve artiq sistemde var!";
        }

        Fruits fruit = new Fruits();
        fruit.setName(fruitsDto.getName());
        fruit.setQiymet(fruitsDto.getQiymet());
        fruit.setMiqdar(fruitsDto.getMiqdar());

        fruitRepository.save(fruit);

        return "Meyve ugurla elave edildi!";
    }

    public Object getFruitsById(UUID id) {
        Optional<Fruits> optionalFruits = fruitRepository.findById(id);

        if (optionalFruits.isPresent()) {
            return ResponseEntity.ok(optionalFruits.get());
        } else {
            return ResponseEntity.ok("Mehsul tapılmadı!: ID = " + id);
        }
    }

    public ResponseEntity<String> updateFruitsById(UUID id, Fruits updatedFruit) {
        Optional<Fruits> optionalFruits = fruitRepository.findById(id);
        if (optionalFruits.isPresent()) {
            Fruits fruit = optionalFruits.get();
            fruit.setName(updatedFruit.getName());
            fruit.setMiqdar(updatedFruit.getMiqdar());
            fruit.setQiymet(updatedFruit.getQiymet());
            fruitRepository.save(fruit);
            return ResponseEntity.ok("Mehsul uğurla yeniləndi!");
        } else {
            return ResponseEntity.ok("Mehsul tapılmadı!");
        }

    }

    public ResponseEntity<String> patchFruitsById(UUID id, Map<String, Object> updates) {
        Optional<Fruits> optionalFruits = fruitRepository.findById(id);
        if (optionalFruits.isPresent()) {
            Fruits fruit = optionalFruits.get();

            updates.forEach((key, value) -> {
                switch (key) {
                    case "name" -> fruit.setName((String) value);
                    case "miqdar" -> fruit.setMiqdar(Double.parseDouble((String) value));
                    case "qiymet" -> fruit.setQiymet(Double.parseDouble((String) value));
                }
            });

            fruitRepository.save(fruit);
            return ResponseEntity.ok("Mehsulun məlumatları qismən yeniləndi!");
        } else {
            return ResponseEntity.ok("Mehsul tapılmadı!");
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
