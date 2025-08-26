package com.example.FruitApp.repository;

import com.example.FruitApp.model.Satislar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SatislarRepository extends JpaRepository<Satislar, UUID> {

}