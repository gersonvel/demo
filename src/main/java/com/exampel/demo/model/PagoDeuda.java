package com.exampel.demo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pago_deudas")
public class PagoDeuda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount; // Cuánto pagó
    private LocalDate date; // Cuándo pagó
    private String note; // Ej: "Abono extra de aguinaldo"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deuda_id", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "user" })
    private Deuda deuda;
}
