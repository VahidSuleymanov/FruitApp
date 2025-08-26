package com.example.FruitApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "kapitalbank")
public class OdenisKartlari {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 16)
    private String cardNumber;

    @Column(nullable = false)
    @Builder.Default
    private YearMonth expiryDate = YearMonth.now().plusYears(3);

    @Column(nullable = false, length = 3)
    private String cvv;

    @Builder.Default
    private double balans = 1000;

    @ManyToOne
    @JoinColumn(name = "odenis_sistem_id")
    private OdenisSistemleri odenisSistemId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}