package com.exampel.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exampel.demo.dto.ResponseDTO;
import com.exampel.demo.model.AhorroHistorial;
import com.exampel.demo.service.AhorroHistorialService;

@RestController
@RequestMapping("/api/ahorros-historial")
// @CrossOrigin(origins = "http://localhost:3000")
public class AhorroHistorialController {

    @Autowired
    private AhorroHistorialService service;

    @PostMapping("/registrar/{savingId}/usuario/{userId}")
    public ResponseEntity<ResponseDTO> registrar(@PathVariable Long savingId, @PathVariable Long userId,
            @RequestBody AhorroHistorial transaccion) {
        try {
            AhorroHistorial nueva = service.registrarTransaccion(savingId, transaccion, userId);
            return ResponseEntity.ok(new ResponseDTO(200, false, "Movimiento registrado", nueva));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ResponseDTO(400, true, e.getMessage(), null));
        }
    }

    @GetMapping("/meta/{savingId}/usuario/{userId}")
    public ResponseEntity<ResponseDTO> listar(@PathVariable Long savingId, @PathVariable Long userId) {
        return ResponseEntity
                .ok(new ResponseDTO(200, false, "Historial obtenido", service.listarPorMeta(savingId, userId)));
    }
}
