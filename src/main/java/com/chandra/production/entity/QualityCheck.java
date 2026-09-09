package com.chandra.production.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quality_checks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QualityCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_id", nullable = false)
    private Production production;

    @Column(nullable = false)
    private Double checkedQuantity;

    @Column(nullable = false)
    private Double passedQuantity;

    @Column(nullable = false)
    private Double failedQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QualityStatus status;

    private String remarks;

    @Column(nullable = false)
    private LocalDateTime checkDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();

        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }

        if (passedQuantity == null) {
            passedQuantity = 0.0;
        }

        if (failedQuantity == null) {
            failedQuantity = 0.0;
        }

        if (status == null) {
            status = QualityStatus.PENDING;
        }

        if (checkDate == null) {
            checkDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}