package com.exampel.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class PruebaController {

    @GetMapping("/hola")
    public String saludo() {
        return "¡Hola! Si puedes ver esto es porque tu token es válido.";
    }
}
