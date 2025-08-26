package com.example.FruitApp.repository;

import com.example.FruitApp.model.Kataqoriyalar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface KataqoriyaRepository extends JpaRepository<Kataqoriyalar, UUID> {

    Optional<Kataqoriyalar> findByName(String name);
    Optional<Kataqoriyalar> findByDescription(String description);

}