package com.example.FruitApp.seeder;

import com.example.FruitApp.model.Statuses;
import com.example.FruitApp.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StatusSeeder implements CommandLineRunner {

    private final StatusRepository statusRepository;

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

        if (statusRepository.findByName("ONLINE").isEmpty()) {
            statusRepository.saveAll(List.of(
                    Statuses.builder().name("ONLINE").description("Bu user halhazirda online`dir").statusId(defaultStatus.getId()).build(),
                    Statuses.builder().name("OFFLINE").description("Bu user halhazirda offline`dir").statusId(defaultStatus.getId()).build(),
                    Statuses.builder().name("FRESH").description("Bu meyve yenidir").statusId(defaultStatus.getId()).build()
            ));
        }
    }

}
