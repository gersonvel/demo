package com.exampel.demo.service;

import java.math.BigDecimal;
import java.util.List;

import com.exampel.demo.model.Ahorro;

public interface AhorroService {
    List<Ahorro> listarPorUsuario(Long userId);

    Ahorro guardar(Ahorro ahorro, Long userId);

    Ahorro actualizarSaldo(Long id, BigDecimal montoNuevo, Long userId);

    void eliminar(Long id, Long userId);

    public Ahorro actualizarMetaCompleta(Long id, Ahorro datosNuevos, Long userId);
}
