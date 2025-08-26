package com.example.FruitApp.seeder;

import com.example.FruitApp.model.OdenisKartlari;
import com.example.FruitApp.model.OdenisSistemleri;
import com.example.FruitApp.model.Statuses;
import com.example.FruitApp.repository.OdenisKartlarRepository;
import com.example.FruitApp.repository.OdenisSistemRepository;
import com.example.FruitApp.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OdenisKartlariSeeder implements CommandLineRunner {

    private final OdenisSistemRepository sistemRepository;

    private final OdenisKartlarRepository kartlarRepository;

    private final StatusRepository statusRepository;

    @Override
    public void run(String... args) {

        Statuses active = statusRepository.findByName("ACTIVE")
                .orElseGet(() -> {
                    Statuses newActive = Statuses.builder()
                            .name("ACTIVE")
                            .description("Bu status halhazirda aktivdir")
                            .build();
                    statusRepository.save(newActive);
                    newActive.setStatusId(newActive.getId());
                    statusRepository.save(newActive);
                    return newActive;
                });

        OdenisSistemleri visa = sistemRepository.findByName("VISA")
                .orElseGet(() -> {
                    OdenisSistemleri s = OdenisSistemleri.builder()
                            .name("VISA")
                            .description("Visa kartı ilə ödəniş")
                            .icon("iconVISA")
                            .statusId(active)
                            .build();
                    sistemRepository.save(s);
                    return s;
                });

        OdenisSistemleri master = sistemRepository.findByName("MasterCard")
                .orElseGet(() -> {
                    OdenisSistemleri s = OdenisSistemleri.builder()
                            .name("MasterCard")
                            .description("MasterCard kartı ilə ödəniş")
                            .icon("iconMasterCard")
                            .statusId(active)
                            .build();
                    sistemRepository.save(s);
                    return s;
                });

        OdenisSistemleri paypal = sistemRepository.findByName("PayPal")
                .orElseGet(() -> {
                    OdenisSistemleri s = OdenisSistemleri.builder()
                            .name("PayPal")
                            .description("PayPal kartı ilə ödəniş")
                            .icon("iconPayPal")
                            .statusId(active)
                            .build();
                    sistemRepository.save(s);
                    return s;
                });

        if (kartlarRepository.findByCardNumber("1111222233334444").isEmpty()) {
            kartlarRepository.saveAll(List.of(
                    OdenisKartlari.builder().cardNumber("1111222233334444").cvv("123").odenisSistemId(visa).build(),
                    OdenisKartlari.builder().cardNumber("2222333344445555").cvv("234").odenisSistemId(master).build(),
                    OdenisKartlari.builder().cardNumber("3333444455556666").cvv("345").odenisSistemId(paypal).build()
            ));
        }

    }

}
