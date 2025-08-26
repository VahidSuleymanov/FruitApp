package com.example.FruitApp.service;

import com.example.FruitApp.dto.ValyutaDto;
import com.example.FruitApp.model.Statuses;
import com.example.FruitApp.model.Valyutalar;
import com.example.FruitApp.repository.StatusRepository;
import com.example.FruitApp.repository.ValyutaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final StatusRepository statusRepository;

    public List<Valyutalar> getAllCurrency() {
        return valyutaRepository.findAll();
    }

    public Object saveCurrency(ValyutaDto valyutaDto) {
        Optional<Valyutalar> existingCurrency = valyutaRepository.findByName(valyutaDto.getName());
        if (existingCurrency.isPresent()) {
            return "Bu valyuta artiq sistemde var!";
        }

        Optional<Valyutalar> existingCurrencyByAbbv = valyutaRepository.findByAbbv(valyutaDto.getAbbv().toUpperCase());
        if (existingCurrencyByAbbv.isPresent()) {
            return "Bu abreviatura artiq sistemde var!";
        }

        Statuses status = statusRepository.findById(valyutaDto.getStatusId())
                .orElseThrow(() -> new RuntimeException("Status tapılmadı"));

        Valyutalar valyuta = Valyutalar.builder()
                .name(valyutaDto.getName())
                .abbv(valyutaDto.getAbbv().toUpperCase())
                .statusId(status)
                .build();

        valyutaRepository.save(valyuta);

        return "Valyuta ugurla sisteme yazildi!";
    }

    public Object getCurrencyById(UUID id) {
        Optional<Valyutalar> optionalCurrency = valyutaRepository.findById(id);

        if (optionalCurrency.isPresent()) {
            return ResponseEntity.ok(optionalCurrency.get());
        } else {
            return ResponseEntity.ok("Valyuta tapılmadı!: ID = " + id);
        }
    }

    public ResponseEntity<String> updateCurrencyById(UUID id, ValyutaDto updatedValyuta) {
        Optional<Valyutalar> optionalCurrency = valyutaRepository.findById(id);
        if (optionalCurrency.isPresent()) {
            Valyutalar valyuta = optionalCurrency.get();

            Optional<Valyutalar> optionalCurrencyByName = valyutaRepository.findByName(updatedValyuta.getName());
            if (optionalCurrencyByName.isPresent() && !optionalCurrencyByName.get().getId().equals(valyuta.getId())) {
                return ResponseEntity.ok("Bu valyuta artıq sistemdə var!");
            }
            valyuta.setName(updatedValyuta.getName());

            Optional<Valyutalar> optionalCurrencyByAbbv = valyutaRepository.findByAbbv(updatedValyuta.getAbbv().toUpperCase());
            if (optionalCurrencyByAbbv.isPresent() && !optionalCurrencyByAbbv.get().getId().equals(valyuta.getId())) {
                return ResponseEntity.ok("Bu abreviatura artıq sistemdə var!");
            }
            valyuta.setAbbv(updatedValyuta.getAbbv().toUpperCase());

            Optional<Statuses> statusOptional = statusRepository.findById(updatedValyuta.getStatusId());
            if (statusOptional.isEmpty()) {
                return ResponseEntity.ok("Belə bir status mövcud deyil!");
            }
            valyuta.setStatusId(statusOptional.get());

            valyutaRepository.save(valyuta);
            return ResponseEntity.ok("Valyuta uğurla yeniləndi!");
        } else {
            return ResponseEntity.ok("Valyuta tapılmadı!");
        }
    }

    public ResponseEntity<String> patchCurrencyById(UUID id, Map<String, Object> updates) {
        Optional<Valyutalar> optionalCurrency = valyutaRepository.findById(id);
        if (optionalCurrency.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Valyuta tapılmadı!");
        }

        Valyutalar valyuta = optionalCurrency.get();
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
                    Optional<Valyutalar> nameOwner = valyutaRepository.findByName(newName);
                    if (nameOwner.isPresent() && !nameOwner.get().getId().equals(valyuta.getId())) {
                        return ResponseEntity.badRequest().body("Bu valyuta artıq sistemdə mövcuddur!");
                    }
                    if (!newName.equals(valyuta.getName())) {
                        valyuta.setName(newName);
                        updated = true;
                    }
                }

                case "abbv" -> {
                    if (value == null || !(value instanceof String) || ((String) value).isBlank()) {
                        break;
                    }
                    String newAbbv = ((String) value).toUpperCase();
                    Optional<Valyutalar> abbvOwner = valyutaRepository.findByAbbv(newAbbv);
                    if (abbvOwner.isPresent() && !abbvOwner.get().getId().equals(valyuta.getId())) {
                        return ResponseEntity.badRequest().body("Bu abreviatura artıq sistemdə mövcuddur!");
                    }
                    if (!newAbbv.equals(valyuta.getAbbv())) {
                        valyuta.setAbbv(newAbbv);
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
                        if (!optionalStatus.get().equals(valyuta.getStatusId())) {
                            valyuta.setStatusId(optionalStatus.get());
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
            valyutaRepository.save(valyuta);
            return ResponseEntity.ok("Valyuta məlumatları qismən yeniləndi!");
        } else {
            return ResponseEntity.ok("Heç bir məlumat yenilənmədi.");
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
