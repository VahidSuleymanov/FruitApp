package com.example.FruitApp.repository;

import com.example.FruitApp.model.Fruits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface FruitsRepository extends JpaRepository<Fruits, UUID> {

    Optional<Fruits> findByName(String name);

}