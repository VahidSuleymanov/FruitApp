package com.example.FruitApp.repository;

import com.example.FruitApp.model.Sekiller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface SekilRepository extends JpaRepository<Sekiller, UUID> {

    Optional<Sekiller> findByName(String name);

}