package com.example.FruitApp.repository;

import com.example.FruitApp.model.OdenisSistemleri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface OdenisSistemRepository extends JpaRepository<OdenisSistemleri, UUID> {

    Optional<OdenisSistemleri> findByName(String name);
    Optional<OdenisSistemleri> findByDescription(String description);
    Optional<OdenisSistemleri> findByIcon(String icon);

}