package com.exampel.demo.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exampel.demo.dto.ResponseDTO;
import com.exampel.demo.model.Ahorro;
import com.exampel.demo.service.AhorroService;

@RestController
@RequestMapping("/api/ahorros")
// @CrossOrigin(origins = "http://localhost:3000")
public class AhorroController {

    @Autowired
    private AhorroService service;

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<ResponseDTO> listar(@PathVariable Long userId) {
        return ResponseEntity.ok(new ResponseDTO(200, false, "Metas de ahorro", service.listarPorUsuario(userId)));
    }

    @PostMapping("/usuario/{userId}")
    public ResponseEntity<ResponseDTO> crear(@RequestBody Ahorro ahorro, @PathVariable Long userId) {
        return ResponseEntity.ok(new ResponseDTO(200, false, "Meta creada", service.guardar(ahorro, userId)));
    }

    // Para actualizar el progreso de ahorro
    @PostMapping("/actualizar-saldo/{id}/usuario/{userId}")
    public ResponseEntity<ResponseDTO> actualizarSaldo(
            @PathVariable Long id,
            @PathVariable Long userId,
            @RequestBody Map<String, BigDecimal> body) {
        BigDecimal nuevoMonto = body.get("currentAmount");
        return ResponseEntity
                .ok(new ResponseDTO(200, false, "Saldo actualizado", service.actualizarSaldo(id, nuevoMonto, userId)));
    }

    @DeleteMapping("/{id}/usuario/{userId}")
    public ResponseEntity<ResponseDTO> eliminar(@PathVariable Long id, @PathVariable Long userId) {
        try {
            service.eliminar(id, userId);
            return ResponseEntity.ok(new ResponseDTO(
                    HttpStatus.OK.value(),
                    false,
                    "Meta de ahorro eliminada con éxito",
                    null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                    HttpStatus.NOT_FOUND.value(),
                    true,
                    e.getMessage(),
                    null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    true,
                    "Error al intentar eliminar la meta de ahorro",
                    null));
        }
    }
}
