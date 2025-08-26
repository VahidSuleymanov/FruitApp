package com.example.FruitApp.repository;

import com.example.FruitApp.model.OdenisKartlari;
import com.example.FruitApp.model.OdenisSistemleri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface OdenisKartlarRepository extends JpaRepository<OdenisKartlari, UUID> {

    Optional<OdenisKartlari> findByCardNumber(String cardNumber);
    Optional<OdenisKartlari> findByCardNumberAndOdenisSistemId(String cardNumber, OdenisSistemleri odenisSistemId);

}