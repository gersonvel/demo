package com.exampel.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exampel.demo.dto.DashboardDTO;
import com.exampel.demo.dto.GastoResumenDTO;
import com.exampel.demo.model.Ahorro;
import com.exampel.demo.model.Deuda;
import com.exampel.demo.repository.AhorroRepository;
import com.exampel.demo.repository.DeudaRepository;
import com.exampel.demo.repository.GastoRepository;

@Service
public class DashboardServiceImpl implements DashboardService {

        @Autowired
        private GastoRepository gastoRepository;
        @Autowired
        private DeudaRepository deudaRepository;
        @Autowired
        private AhorroRepository ahorroRepository;

        @Override
        public DashboardDTO obtenerResumen(Long userId) {
                // 1. Ahorros y Deudas (Suma de streams)
                BigDecimal totalAhorrado = ahorroRepository.findByUserId(userId).stream()
                                .map(Ahorro::getCurrentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalDeuda = deudaRepository.findByUserId(userId).stream()
                                .map(Deuda::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);

                // 2. Traer categorías filtradas por MES y AÑO ACTUAL usando el nuevo método
                List<GastoResumenDTO> resumenGastos = gastoRepository.sumGastosPorCategoriaMesActual(userId);

                // 3. Convertir el DTO del repository al formato de mapa para el DashboardDTO
                List<Map<String, Object>> gastosPorCat = resumenGastos.stream().map(dto -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("nombre", dto.getRubro());
                        map.put("valor", dto.getTotal());
                        return map;
                }).collect(Collectors.toList());

                // 4. Calcular el total sumando los valores del DTO (Dará 6770.00)
                BigDecimal gastosMesActual = resumenGastos.stream()
                                .map(GastoResumenDTO::getTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                return new DashboardDTO(
                                totalAhorrado,
                                totalDeuda,
                                totalAhorrado.subtract(totalDeuda),
                                gastosMesActual,
                                gastosPorCat);
        }

        @Override
        public DashboardDTO obtenerResumenPorFecha(Long userId, int mes, int anio) {
                // Los ahorros y deudas suelen ser el estado "actual",
                // pero los gastos sí dependen de la fecha seleccionada.

                BigDecimal totalAhorrado = ahorroRepository.findByUserId(userId).stream()
                                .map(Ahorro::getCurrentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalDeuda = deudaRepository.findByUserId(userId).stream()
                                .map(Deuda::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);

                // Filtramos los gastos por la fecha que viene del Front
                List<GastoResumenDTO> resumenGastos = gastoRepository.sumGastosPorMesYAnio(userId, mes, anio);

                List<Map<String, Object>> gastosPorCat = resumenGastos.stream().map(dto -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("nombre", dto.getRubro());
                        map.put("valor", dto.getTotal());
                        return map;
                }).collect(Collectors.toList());

                BigDecimal gastosMesSeleccionado = resumenGastos.stream()
                                .map(GastoResumenDTO::getTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                return new DashboardDTO(
                                totalAhorrado,
                                totalDeuda,
                                totalAhorrado.subtract(totalDeuda),
                                gastosMesSeleccionado,
                                gastosPorCat);
        }

        @Override
        public DashboardDTO obtenerResumenPorDia(Long userId, int dia, int mes, int anio) {
                // Totales actuales de ahorros y deudas
                BigDecimal totalAhorrado = ahorroRepository.findByUserId(userId).stream()
                                .map(Ahorro::getCurrentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalDeuda = deudaRepository.findByUserId(userId).stream()
                                .map(Deuda::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);

                // Consulta específica por día
                List<GastoResumenDTO> resumenGastos = gastoRepository.sumGastosPorDiaMesYAnio(userId, dia, mes, anio);

                List<Map<String, Object>> gastosPorCat = resumenGastos.stream().map(dto -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("nombre", dto.getRubro());
                        map.put("valor", dto.getTotal());
                        return map;
                }).collect(Collectors.toList());

                BigDecimal totalDelDia = resumenGastos.stream()
                                .map(GastoResumenDTO::getTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                return new DashboardDTO(
                                totalAhorrado,
                                totalDeuda,
                                totalAhorrado.subtract(totalDeuda),
                                totalDelDia,
                                gastosPorCat);
        }
}
