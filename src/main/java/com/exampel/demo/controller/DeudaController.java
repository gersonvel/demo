package com.exampel.demo.controller;

import java.math.BigDecimal;
import java.util.List;
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
import com.exampel.demo.model.Deuda;
import com.exampel.demo.model.PagoDeuda;
import com.exampel.demo.service.DeudaService;

@RestController
@RequestMapping("/api/deudas")
// @CrossOrigin(origins = "http://localhost:3000")
public class DeudaController {

    @Autowired
    private DeudaService deudaService;

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<ResponseDTO> listar(@PathVariable Long userId) {
        List<Deuda> deudas = deudaService.listarPorUsuario(userId);

        // Opcional: Podrías mapear esto a un DTO para incluir la fecha calculada
        return ResponseEntity.ok(new ResponseDTO(200, false, "Deudas obtenidas", deudas));
    }

    @GetMapping("/{id}/usuario/{userId}")
    public ResponseEntity<ResponseDTO> obtenerUna(@PathVariable Long id, @PathVariable Long userId) {
        try {
            Deuda deuda = deudaService.obtenerPorId(id, userId);
            return ResponseEntity.ok(new ResponseDTO(200, false, "Deuda obtenida", deuda));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ResponseDTO(404, true, e.getMessage(), null));
        }
    }

    @PostMapping("/usuario/{userId}")
    public ResponseEntity<ResponseDTO> guardar(@RequestBody Deuda deuda, @PathVariable Long userId) {
        try {
            Deuda nueva = deudaService.guardar(deuda, userId);
            return ResponseEntity.ok(new ResponseDTO(200, false, "Deuda registrada", nueva));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO(500, true, e.getMessage(), null));
        }
    }

    // Endpoint para actualizar (usando POST por tu preferencia)
    @PostMapping("/actualizar/{id}/usuario/{userId}")
    public ResponseEntity<ResponseDTO> actualizar(@PathVariable Long id, @PathVariable Long userId,
            @RequestBody Deuda deuda) {
        try {
            Deuda actualizada = deudaService.actualizar(id, deuda, userId);
            return ResponseEntity.ok(new ResponseDTO(200, false, "Deuda actualizada", actualizada));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ResponseDTO(400, true, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}/usuario/{userId}")
    public ResponseEntity<ResponseDTO> eliminar(@PathVariable Long id, @PathVariable Long userId) {
        try {
            deudaService.eliminar(id, userId);
            return ResponseEntity.ok(new ResponseDTO(
                    200,
                    false,
                    "Deuda eliminada correctamente",
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
                    "Error al eliminar la deuda",
                    null));
        }
    }

    @PostMapping("/{deudaId}/abonar/usuario/{userId}")
    public ResponseEntity<ResponseDTO> abonar(
            @PathVariable Long deudaId,
            @PathVariable Long userId,
            @RequestBody Map<String, Object> payload) {
        try {
            BigDecimal monto = new BigDecimal(payload.get("monto").toString());
            String nota = payload.get("nota").toString();

            PagoDeuda nuevoPago = deudaService.registrarPago(deudaId, monto, nota, userId);

            return ResponseEntity.ok(new ResponseDTO(200, false, "Abono realizado con éxito", nuevoPago));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ResponseDTO(400, true, e.getMessage(), null));
        }
    }

    @GetMapping("/{deudaId}/pagos/usuario/{userId}")
    public ResponseEntity<ResponseDTO> listarPagos(@PathVariable Long deudaId, @PathVariable Long userId) {
        try {
            List<PagoDeuda> pagos = deudaService.listarPagosPorDeuda(deudaId, userId);
            return ResponseEntity.ok(new ResponseDTO(200, false, "Historial de pagos obtenido", pagos));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ResponseDTO(404, true, e.getMessage(), null));
        }
    }
}
