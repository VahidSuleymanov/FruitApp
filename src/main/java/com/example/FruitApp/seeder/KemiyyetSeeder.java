package com.example.FruitApp.seeder;

import com.example.FruitApp.model.Kemiyyetler;
import com.example.FruitApp.model.Statuses;
import com.example.FruitApp.repository.KemiyyetRepository;
import com.example.FruitApp.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KemiyyetSeeder implements CommandLineRunner {

    private final StatusRepository statusRepository;

    private final KemiyyetRepository kemiyyetRepository;

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

        if (kemiyyetRepository.findByAbbv("kq").isEmpty()) {
            kemiyyetRepository.saveAll(List.of(
                    Kemiyyetler.builder().name("Kiloqram").abbv("kq").statusId(defaultStatus).build(),
                    Kemiyyetler.builder().name("Ədəd").abbv("ədəd").statusId(defaultStatus).build(),
                    Kemiyyetler.builder().name("Litr").abbv("l").statusId(defaultStatus).build()
            ));
        }

    }

}
