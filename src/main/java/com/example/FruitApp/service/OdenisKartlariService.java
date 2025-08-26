package com.example.FruitApp.service;

import com.example.FruitApp.dto.OdenisKartlariDto;
import com.example.FruitApp.model.OdenisKartlari;
import com.example.FruitApp.model.OdenisSistemleri;
import com.example.FruitApp.repository.OdenisKartlarRepository;
import com.example.FruitApp.repository.OdenisSistemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OdenisKartlariService {

    private final OdenisKartlarRepository odenisKartlariRepository;

    private final OdenisSistemRepository sistemRepository;


    public List<OdenisKartlari> getAllCarts() {
        return odenisKartlariRepository.findAll();
    }

    public Object saveCarts(OdenisKartlariDto cartsDto) {
        Optional<OdenisKartlari> existingCarts = odenisKartlariRepository.findByCardNumber(cartsDto.getCardNumber());
        if (existingCarts.isPresent()) {
            return "Bu odenis karti artiq sistemde var!";
        }

        OdenisSistemleri sistems = sistemRepository.findById(cartsDto.getOdenisSistemId())
                .orElseThrow(() -> new RuntimeException("Odenis sistemi tapılmadı"));

        OdenisKartlari carts = OdenisKartlari.builder()
                .cardNumber(cartsDto.getCardNumber())
                .expiryDate(cartsDto.getExpiryDate())
                .cvv(cartsDto.getCvv())
                .balans(cartsDto.getBalans())
                .odenisSistemId(sistems)
                .build();

        odenisKartlariRepository.save(carts);

        return "Odenis karti ugurla sisteme yazildi!";
    }


    public String deleteCartById(UUID id) {
        if (!odenisKartlariRepository.existsById(id)) {
            return "Bu ID: " + id + " uzre odenis karti tapilmadi!";
        }

        odenisKartlariRepository.deleteById(id);
        return "Odenis karti silindi!";
    }

}
