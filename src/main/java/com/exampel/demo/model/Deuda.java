package com.exampel.demo.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
@Table(name = "deudas")
public class Deuda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Ej: "Crédito Automotriz"
    private BigDecimal totalAmount; // Monto inicial de la deuda
    private BigDecimal balance; // Lo que falta por pagar hoy
    private BigDecimal monthlyPayment; // Cuánto pagas cada mes

    private Integer dueDateDay; // El día del mes (ej: 15) para alertas
    private LocalDate startDate; // Cuándo empezó la deuda

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "password", "hibernateLazyInitializer", "handler" })
    private Usuario user;

    /**
     * Método de ayuda para el Dashboard de Next.js
     * Calcula cuántos meses faltan basados en el balance actual
     */
    public int getMonthsRemaining() {
        if (monthlyPayment.compareTo(BigDecimal.ZERO) <= 0)
            return 0;
        return balance.divide(monthlyPayment, RoundingMode.CEILING).intValue();
    }
}
