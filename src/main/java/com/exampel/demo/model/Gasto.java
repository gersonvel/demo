package com.exampel.demo.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gastos")
public class Gasto {
    // @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    // private UUID id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private BigDecimal amount;
    private LocalDate date;

    @ManyToOne
    private Categoria category; // Relación con el rubro

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "password", "hibernateLazyInitializer", "handler" })
    private Usuario user;

    // Opcional: Para saber si este gasto fue el pago de una deuda
    @ManyToOne
    @JoinColumn(name = "deuda_id")
    private Deuda relatedDebt;
}
