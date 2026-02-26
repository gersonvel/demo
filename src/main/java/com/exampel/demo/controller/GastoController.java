package com.exampel.demo.controller;

import com.exampel.demo.dto.ResponseDTO;
import com.exampel.demo.model.Gasto;
import com.exampel.demo.service.GastoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gastos")
// @CrossOrigin(origins = "http://localhost:3000")
public class GastoController {

    @Autowired
    private GastoService gastoService;

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<ResponseDTO> listar(@PathVariable Long userId) {
        return ResponseEntity.ok(new ResponseDTO(
                HttpStatus.OK.value(),
                false,
                "Gastos obtenidos",
                gastoService.listarPorUsuario(userId)));
    }

    @GetMapping("/{id}/usuario/{userId}")
    public ResponseEntity<ResponseDTO> obtenerUno(@PathVariable Long id, @PathVariable Long userId) {
        try {
            Gasto gasto = gastoService.obtenerPorId(id, userId);
            return ResponseEntity.ok(new ResponseDTO(200, false, "Gasto obtenido", gasto));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ResponseDTO(404, true, e.getMessage(), null));
        }
    }

    @PostMapping("/usuario/{userId}")
    public ResponseEntity<ResponseDTO> crear(@RequestBody Gasto gasto, @PathVariable Long userId) {
        try {
            Gasto nuevo = gastoService.guardar(gasto, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(
                    HttpStatus.OK.value(),
                    false,
                    "Gasto registrado",
                    nuevo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    true,
                    e.getMessage(),
                    null));
        }
    }

    // Endpoint especial para la gráfica de pastel del Dashboard
    @GetMapping("/resumen/categorias/{userId}")
    public ResponseEntity<ResponseDTO> obtenerResumen(@PathVariable Long userId) {
        return ResponseEntity.ok(new ResponseDTO(
                HttpStatus.OK.value(),
                false,
                "Resumen por categoría obtenido",
                gastoService.resumenPorCategoria(userId)));
    }

    @PostMapping("/actualizar/{id}/usuario/{userId}") // Usamos POST como prefieres
    public ResponseEntity<ResponseDTO> actualizar(@PathVariable Long id, @PathVariable Long userId,
            @RequestBody Gasto gasto) {
        try {
            Gasto actualizado = gastoService.actualizar(id, gasto, userId);
            return ResponseEntity.ok(new ResponseDTO(200, false, "Gasto actualizado", actualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(400, true, e.getMessage(), null));
        }
    }

    // 1. Endpoint para Paginación
    @GetMapping("/usuario/{userId}/paginado")
    public ResponseEntity<ResponseDTO> listarPaginado(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(new ResponseDTO(
                200,
                false,
                "Página de gastos obtenida",
                gastoService.listarPaginado(userId, page, size)));
    }

    // 2. Endpoint para Filtro por Día/Mes/Año
    @GetMapping("/usuario/{userId}/filtro-fecha")
    public ResponseEntity<ResponseDTO> listarPorFecha(
            @PathVariable Long userId,
            @RequestParam int dia,
            @RequestParam int mes,
            @RequestParam int anio) {

        return ResponseEntity.ok(new ResponseDTO(
                200,
                false,
                "Gastos del día " + dia + "/" + mes + "/" + anio + " obtenidos",
                gastoService.listarPorFechaExacta(userId, dia, mes, anio)));
    }
}
