package com.exampel.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class DashboardDTO {
    private BigDecimal totalAhorrado;
    private BigDecimal totalDeuda;
    private BigDecimal patrimonioNeto; // Ahorro - Deuda
    private BigDecimal gastosMesActual;
    private List<Map<String, Object>> gastosPorCategoria; // Para la gráfica de pastel
}
