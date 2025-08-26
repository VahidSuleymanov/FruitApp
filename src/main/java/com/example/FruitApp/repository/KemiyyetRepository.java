package com.example.FruitApp.repository;

import com.example.FruitApp.model.Kemiyyetler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface KemiyyetRepository extends JpaRepository<Kemiyyetler, UUID> {

    Optional<Kemiyyetler> findByName(String name);
    Optional<Kemiyyetler> findByAbbv(String abbv);

}