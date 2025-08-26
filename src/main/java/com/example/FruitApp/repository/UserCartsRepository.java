package com.example.FruitApp.repository;

import com.example.FruitApp.model.OdenisSistemleri;
import com.example.FruitApp.model.UserCarts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCartsRepository extends JpaRepository<UserCarts, UUID> {

    Optional<UserCarts> findByUserId_IdAndCardNumberAndOdenisSistemId(UUID userId, String cardNumber, OdenisSistemleri odenisSistemId);

}