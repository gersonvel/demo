package com.exampel.demo.service;

import java.util.List;

import com.exampel.demo.model.AhorroHistorial;

public interface AhorroHistorialService {
    AhorroHistorial registrarTransaccion(Long savingId, AhorroHistorial transaccion, Long userId);

    List<AhorroHistorial> listarPorMeta(Long savingId, Long userId);
}
