package com.exampel.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ahorros")
public class Ahorro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Ej: "Fondo de Emergencia"

    @Column(nullable = false)
    private BigDecimal targetAmount; // Cuánto quieres ahorrar en total

    @Column(nullable = false)
    private BigDecimal currentAmount; // Cuánto llevas ahorrado actualmente

    private String icon; // Icono para el Dashboard
    private String color; // Color para la barra de progreso

    private LocalDate deadline; // Fecha opcional para cumplir la meta

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "password", "hibernateLazyInitializer", "handler" })
    private Usuario user;

    /**
     * Calcula el porcentaje de progreso para el Dashboard de Next.js.
     * Esto es muy útil para mostrar barras de progreso (Progress Bars).
     */
    // public double getProgressPercentage() {
    // if (targetAmount.compareTo(BigDecimal.ZERO) == 0)
    // return 0.0;
    // return (currentAmount.doubleValue() / targetAmount.doubleValue()) * 100;
    // }
    public double getProgress() {
        if (targetAmount.compareTo(BigDecimal.ZERO) == 0)
            return 0;
        return currentAmount.multiply(new BigDecimal(100))
                .divide(targetAmount, 2, RoundingMode.HALF_UP).doubleValue();
    }
}