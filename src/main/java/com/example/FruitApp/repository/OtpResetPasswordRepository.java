package com.example.FruitApp.repository;

import com.example.FruitApp.model.OtpResetPassword;
import com.example.FruitApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpResetPasswordRepository extends JpaRepository<OtpResetPassword, UUID> {

    Optional<OtpResetPassword> findByUserId(User userId);
    void deleteByUserId(User userId);

}