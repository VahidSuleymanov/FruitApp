package com.example.FruitApp.seeder;

import com.example.FruitApp.model.Statuses;
import com.example.FruitApp.model.Valyutalar;
import com.example.FruitApp.repository.StatusRepository;
import com.example.FruitApp.repository.ValyutaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ValyutaSeeder implements CommandLineRunner {

    private final StatusRepository statusRepository;

    private final ValyutaRepository valyutaRepository;

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

        if (valyutaRepository.findByAbbv("AZN").isEmpty()) {
            valyutaRepository.saveAll(List.of(
                    Valyutalar.builder().name("Azerbaycan Manati").abbv("AZN").statusId(defaultStatus).build(),
                    Valyutalar.builder().name("Amerika Dollari").abbv("USD").statusId(defaultStatus).build(),
                    Valyutalar.builder().name("Avropa pul vahidi").abbv("EUR").statusId(defaultStatus).build(),
                    Valyutalar.builder().name("Rusiya rublu").abbv("RUB").statusId(defaultStatus).build()
            ));
        }

    }

}
