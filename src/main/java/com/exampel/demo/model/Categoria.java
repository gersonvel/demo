package com.exampel.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// import org.springframework.boot.security.autoconfigure.SecurityProperties.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories") // Usar plural en tablas es una convención común
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Ej: "Suscripciones", "Comida"

    private String icon; // Nombre del icono (ej: "PizzaIcon" o "HomeIcon")

    private String color; // Ej: "#FF5733" para las gráficas de Next.js

    @ManyToOne(fetch = FetchType.LAZY) // Lazy para no cargar al usuario cada vez que pidas la categoría
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "password", "hibernateLazyInitializer", "handler" }) // no esponer la contraseña
    private Usuario user;
}