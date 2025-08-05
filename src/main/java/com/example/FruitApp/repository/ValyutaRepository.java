package com.example.FruitApp.repository;

import com.example.FruitApp.model.Valyutalar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface ValyutaRepository extends JpaRepository<Valyutalar, UUID> {

    Optional<Valyutalar> findByName(String name);
    Optional<Valyutalar> findByAbbv(String abbv);

}