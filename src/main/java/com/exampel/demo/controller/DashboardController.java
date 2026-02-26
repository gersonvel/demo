package com.exampel.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exampel.demo.dto.DashboardDTO;
import com.exampel.demo.dto.ResponseDTO;
import com.exampel.demo.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
// @CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/resumen/{userId}")
    public ResponseEntity<ResponseDTO> getResumen(@PathVariable Long userId) {
        try {
            DashboardDTO dto = dashboardService.obtenerResumen(userId);
            return ResponseEntity.ok(new ResponseDTO(200, false, "Dashboard cargado", dto));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO(500, true, "Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/resumen-detalle/{userId}")
    public ResponseEntity<ResponseDTO> getResumenPorFecha(
            @PathVariable Long userId,
            @RequestParam int mes,
            @RequestParam int anio) {
        try {
            DashboardDTO dto = dashboardService.obtenerResumenPorFecha(userId, mes, anio);
            return ResponseEntity.ok(new ResponseDTO(200, false, "Dashboard filtrado cargado", dto));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO(500, true, e.getMessage(), null));
        }
    }

    @GetMapping("/resumen-diario/{userId}")
    public ResponseEntity<ResponseDTO> getResumenDiario(
            @PathVariable Long userId,
            @RequestParam int dia,
            @RequestParam int mes,
            @RequestParam int anio) {
        try {
            DashboardDTO dto = dashboardService.obtenerResumenPorDia(userId, dia, mes, anio);
            return ResponseEntity.ok(new ResponseDTO(200, false, "Dashboard diario cargado", dto));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO(500, true, e.getMessage(), null));
        }
    }
}
