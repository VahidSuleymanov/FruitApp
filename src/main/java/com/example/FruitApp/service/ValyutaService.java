package com.example.FruitApp.service;

import com.example.FruitApp.dto.ValyutaDto;
import com.example.FruitApp.enums.Statuses;
import com.example.FruitApp.model.Valyutalar;
import com.example.FruitApp.repository.ValyutaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ValyutaService {

    private final ValyutaRepository valyutaRepository;

    public List<Valyutalar> getAllCurrency() {
        return valyutaRepository.findAll();
    }

    public String saveCurrency(ValyutaDto valyutaDto) {
        Optional<Valyutalar> existingCurrency = valyutaRepository.findByName(valyutaDto.getName());
        if (existingCurrency.isPresent()) {
            return "Bu valyuta artiq sistemde var!";
        }

        Valyutalar valyuta = new Valyutalar();
        valyuta.setName(valyutaDto.getName());
        valyuta.setAbbv(valyutaDto.getAbbv());

        valyutaRepository.save(valyuta);

        return "Valyuta ugurla elave edildi!";
    }

    public Object getCurrencyById(UUID id) {
        Optional<Valyutalar> optionalCurrency = valyutaRepository.findById(id);

        if (optionalCurrency.isPresent()) {
            return ResponseEntity.ok(optionalCurrency.get());
        } else {
            return ResponseEntity.ok("Valyuta tapılmadı!: ID = " + id);
        }
    }

    public ResponseEntity<String> updateCurrencyById(UUID id, Valyutalar updatedValyuta) {
        Optional<Valyutalar> optionalCurrency = valyutaRepository.findById(id);
        if (optionalCurrency.isPresent()) {
            Valyutalar valyuta = optionalCurrency.get();
            valyuta.setName(updatedValyuta.getName());
            valyuta.setAbbv(updatedValyuta.getAbbv());
            valyuta.setStatus(Statuses.valueOf(updatedValyuta.getStatus().toString().toUpperCase()));
            valyutaRepository.save(valyuta);
            return ResponseEntity.ok("Valyuta uğurla yeniləndi!");
        } else {
            return ResponseEntity.ok("Valyuta tapılmadı!");
        }

    }

    public ResponseEntity<String> patchCurrencyById(UUID id, Map<String, Object> updates) {
        Optional<Valyutalar> optionalCurrency = valyutaRepository.findById(id);
        if (optionalCurrency.isPresent()) {
            Valyutalar valyuta = optionalCurrency.get();

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                switch (key) {
                    case "name" -> {
                        String newName = (String) value;
                        Optional<Valyutalar> nameOwner = valyutaRepository.findByName(newName);
                        if (nameOwner.isPresent() && !nameOwner.get().getId().equals(valyuta.getId())) {
                            return ResponseEntity.badRequest().body("Bu valyuta artıq sistemdə mövcuddur!");
                        }
                        valyuta.setName(newName);
                    }

                    case "abbv" -> {
                        String newAbbv = (String) value;
                        Optional<Valyutalar> abbvOwner = valyutaRepository.findByAbbv(newAbbv);
                        if (abbvOwner.isPresent() && !abbvOwner.get().getId().equals(valyuta.getId())) {
                            return ResponseEntity.badRequest().body("Bu abbriviyatura artıq sistemdə mövcuddur!");
                        }
                        valyuta.setAbbv(newAbbv); // düzəliş burada
                    }

                    case "status" -> {
                        try {
                            valyuta.setStatus(Statuses.valueOf(value.toString().toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            return ResponseEntity.badRequest().body("Düzgün status dəyəri göndərilməyib.");
                        }
                    }
                }
            }

            valyutaRepository.save(valyuta);
            return ResponseEntity.ok("Valyuta məlumatları qismən yeniləndi!");
        } else {
            return ResponseEntity.ok("Valyuta tapılmadı!");
        }
    }


    public String deleteCurrencyById(UUID id) {
        if (!valyutaRepository.existsById(id)) {
            return "Bu ID: " + id + " uzre valyuta tapilmadi!";
        }

        valyutaRepository.deleteById(id);
        return "Valyuta silindi!";
    }

}
