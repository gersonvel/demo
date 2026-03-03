package com.exampel.demo.service;

import com.exampel.demo.dto.GastoResumenDTO;
import com.exampel.demo.model.Gasto;
import java.util.List;

import org.springframework.data.domain.Page;

public interface GastoService {
    List<Gasto> listarPorUsuario(Long userId, String categoria);

    Gasto obtenerPorId(Long id, Long userId);

    Gasto guardar(Gasto gasto, Long userId);

    void eliminar(Long id, Long userId);

    Gasto actualizar(Long id, Gasto gasto, Long userId);

    // Para el Dashboard
    List<GastoResumenDTO> resumenPorCategoria(Long userId);

    Page<Gasto> listarPaginado(Long userId, int page, int size);

    List<Gasto> listarPorFechaExacta(Long userId, int dia, int mes, int anio);

    public List<Gasto> listarPorMes(Long userId, int mes, int anio, String categoria);
}