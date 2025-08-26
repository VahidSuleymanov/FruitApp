package com.example.FruitApp.seeder;

import com.example.FruitApp.model.OdenisSistemleri;
import com.example.FruitApp.model.Statuses;
import com.example.FruitApp.repository.OdenisSistemRepository;
import com.example.FruitApp.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OdenisSistemleriSeeder implements CommandLineRunner {

    private final StatusRepository statusRepository;

    private final OdenisSistemRepository sistemRepository;

    @Override
    public void run(String... args) {

        if (statusRepository.findByName("ACTIVE").isEmpty()) {
            Statuses active = Statuses.builder()
                    .name("ACTIVE")
                    .description("Bu status halhazirda aktiv`dir")
                    .build();
            statusRepository.save(active);

            active.setStatusId(active.getId());
            statusRepository.save(active);
        }


        Statuses defaultStatus = statusRepository.findByName("ACTIVE")
                .orElseThrow(() -> new RuntimeException("Default status not found"));

        if (sistemRepository.findByName("VISA").isEmpty()) {
            sistemRepository.saveAll(List.of(
                    OdenisSistemleri.builder().name("VISA").description("Visa kartı ilə ödəniş").icon("iconVISA").statusId(defaultStatus).build(),
                    OdenisSistemleri.builder().name("MasterCard").description("MasterCard kartı ilə ödəniş").icon("iconMasterCard").statusId(defaultStatus).build(),
                    OdenisSistemleri.builder().name("PayPal").description("Paypal kartı ilə ödəniş").icon("iconPayPal").statusId(defaultStatus).build()
            ));
        }

    }

}
