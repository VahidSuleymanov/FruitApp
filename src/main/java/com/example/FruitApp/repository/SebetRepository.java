package com.example.FruitApp.repository;

import com.example.FruitApp.model.Fruits;
import com.example.FruitApp.model.Sebet;
import com.example.FruitApp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SebetRepository extends JpaRepository<Sebet, UUID> {

    Optional<Sebet> findByUserIdAndFruitId(User user, Fruits fruit);
    List<Sebet> findAllByUserId_Id(UUID userId);
    Page<Sebet> findByUserId(User userId, Pageable pageable);

}