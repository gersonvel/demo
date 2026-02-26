package com.exampel.demo.service;

import com.exampel.demo.model.Deuda;
import com.exampel.demo.model.PagoDeuda;

import java.math.BigDecimal;
import java.util.List;

public interface DeudaService {
    List<Deuda> listarPorUsuario(Long userId);

    Deuda obtenerPorId(Long id, Long userId);

    Deuda guardar(Deuda deuda, Long userId);

    void eliminar(Long id, Long userId);

    Deuda actualizar(Long id, Deuda deuda, Long userId);

    PagoDeuda registrarPago(Long deudaId, BigDecimal monto, String nota, Long userId);

    List<PagoDeuda> listarPagosPorDeuda(Long deudaId, Long userId);
}
