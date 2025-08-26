package com.example.FruitApp.seeder;

import com.example.FruitApp.model.Kataqoriyalar;
import com.example.FruitApp.model.Statuses;
import com.example.FruitApp.repository.KataqoriyaRepository;
import com.example.FruitApp.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KataqoriyaSeeder implements CommandLineRunner {

    private final StatusRepository statusRepository;

    private final KataqoriyaRepository kataqoriyaRepository;

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

        if (kataqoriyaRepository.findByName("Meyveler").isEmpty()) {
            kataqoriyaRepository.saveAll(List.of(
                    Kataqoriyalar.builder().name("Meyveler").description("Bu meyve kataqoriyasidir").statusId(defaultStatus).build(),
                    Kataqoriyalar.builder().name("Terevezler").description("Bu terevez kataqoriyasidir").statusId(defaultStatus).build(),
                    Kataqoriyalar.builder().name("Giləmeyvələr").description("Bu gilemeyve kataqoriyasidir").statusId(defaultStatus).build(),
                    Kataqoriyalar.builder().name("Qurudulmuş Məhsullar").description("Bu qurudulmuş məhsul kataqoriyasidir").statusId(defaultStatus).build(),
                    Kataqoriyalar.builder().name("Tropik Meyvələr").description("Bu tropik meyvə kataqoriyasidir").statusId(defaultStatus).build()
            ));
        }

    }

}
