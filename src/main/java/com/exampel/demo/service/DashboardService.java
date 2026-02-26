package com.exampel.demo.service;

import com.exampel.demo.dto.DashboardDTO;

public interface DashboardService {
    DashboardDTO obtenerResumen(Long userId);

    DashboardDTO obtenerResumenPorFecha(Long userId, int mes, int anio);

    DashboardDTO obtenerResumenPorDia(Long userId, int dia, int mes, int anio);
}
