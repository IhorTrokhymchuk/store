package com.example.store.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "order_status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "status",
            nullable = false,
            unique = true,
            columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private StatusName status;

    public enum StatusName {
        COMPLETED,
        PENDING,
        DELIVERED
    }

}
