package com.example.FruitApp.repository;

import com.example.FruitApp.model.Sebet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface SebetRepository extends JpaRepository<Sebet, UUID> {

}