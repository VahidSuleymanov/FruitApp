package com.example.FruitApp.service;

import com.example.FruitApp.dto.UserCartsDto;
import com.example.FruitApp.model.*;
import com.example.FruitApp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OdenisKartlarRepository odenisKartlariRepository;
    private final OdenisSistemRepository odenisSistemleriRepository;
    private final UserCartsRepository userCartRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final SebetRepository sebetRepository;
    private final FruitsRepository fruitsRepository;
    private final SatislarRepository satislarRepository;

    @Transactional
    public String processPayment(UserCartsDto dto, UUID userId) {

        List<Sebet> sebetItems = sebetRepository.findAllByUserId_Id(userId);
        if (sebetItems.isEmpty()) {
            throw new RuntimeException("Sizin səbətiniz boşdur, ödəniş edilə bilməz");
        }

        OdenisSistemleri odenisSistem = odenisSistemleriRepository
                .findByName(dto.getNameOnCart())
                .orElseThrow(() -> new RuntimeException("Ödəniş sistemi tapılmadı"));

        OdenisKartlari bankCard = odenisKartlariRepository
                .findByCardNumberAndOdenisSistemId(dto.getCardNumber(), odenisSistem)
                .orElseThrow(() -> new RuntimeException("Bu ödəniş sistemində belə bir kart tapılmadı"));

        if (!bankCard.getExpiryDate().equals(dto.getExpiryDate())) {
            throw new RuntimeException("Bitmə tarixi yanlışdır");
        }

        if (!bankCard.getCvv().equals(dto.getCvv())) {
            throw new RuntimeException("CVV yanlışdır");
        }

        if (bankCard.getBalans() < dto.getTotalPrice()) {
            throw new RuntimeException("Balans kifayət deyil");
        }

        bankCard.setBalans(bankCard.getBalans() - dto.getTotalPrice());
        odenisKartlariRepository.save(bankCard);

        Statuses defaultStatus = statusRepository.findByName("ACTIVE")
                .orElseThrow(() -> new RuntimeException("Default status not found"));

        String message = "Ödəniş uğurla edildi";

        if (dto.isSaveThisCart()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

            boolean exists = userCartRepository
                    .findByUserId_IdAndCardNumberAndOdenisSistemId(user.getId(), dto.getCardNumber(), odenisSistem)
                    .isPresent();

            if (exists) {
                message = "Bu kart artıq yadda saxlanılıb. " + message;
            } else {
                UserCarts userCart = UserCarts.builder()
                        .userId(user)
                        .odenisSistemId(odenisSistem)
                        .cardNumber(dto.getCardNumber())
                        .expiryDate(dto.getExpiryDate())
                        .cvv(dto.getCvv())
                        .statusId(defaultStatus)
                        .build();

                userCartRepository.save(userCart);
                message = "Kart yadda saxlanıldı. " + message;
            }
        }

        List<Fruits> updatedFruits = new ArrayList<>();
        List<Satislar> salesList = new ArrayList<>();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

        for (Sebet item : sebetItems) {
            Fruits fruit = item.getFruitId();

            if (fruit.getMiqdar() < item.getSay()) {
                throw new RuntimeException("Stok kifayət deyil: " + fruit.getName());
            }

            fruit.setMiqdar(fruit.getMiqdar() - item.getSay());
            updatedFruits.add(fruit);


            Satislar sale = Satislar.builder()
                    .userId(user)
                    .fruitId(fruit)
                    .cardId(bankCard)
                    .say(item.getSay())
                    .totalPrice(item.getSay() * fruit.getQiymet())
                    .statusId(defaultStatus)
                    .build();

            salesList.add(sale);
        }

        fruitsRepository.saveAll(updatedFruits);
        sebetRepository.deleteAll(sebetItems);
        satislarRepository.saveAll(salesList);

        return message;
    }
}
