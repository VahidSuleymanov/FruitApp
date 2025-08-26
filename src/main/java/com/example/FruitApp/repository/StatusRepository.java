package com.example.FruitApp.repository;

import com.example.FruitApp.model.Statuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatusRepository extends JpaRepository<Statuses, UUID> {

    Optional<Statuses> findByName(String name);
    Optional<Statuses> findByDescription(String description);

}