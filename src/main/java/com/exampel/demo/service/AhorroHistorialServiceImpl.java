package com.exampel.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exampel.demo.model.Ahorro;
import com.exampel.demo.model.AhorroHistorial;
import com.exampel.demo.repository.AhorroHistorialRepository;
import com.exampel.demo.repository.AhorroRepository;

import jakarta.transaction.Transactional;

@Service
public class AhorroHistorialServiceImpl implements AhorroHistorialService {

    @Autowired
    private AhorroHistorialRepository historialRepository;

    @Autowired
    private AhorroRepository ahorroRepository;

    @Override
    @Transactional
    public AhorroHistorial registrarTransaccion(Long savingId, AhorroHistorial transaccion, Long userId) {
        // 1. Validar que la meta exista y pertenezca al usuario
        Ahorro ahorro = ahorroRepository.findById(savingId)
                .filter(a -> a.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Meta de ahorro no encontrada"));

        // 2. Actualizar el saldo (currentAmount) de la meta
        if ("RETIRO".equalsIgnoreCase(transaccion.getType())) {
            if (ahorro.getCurrentAmount().compareTo(transaccion.getAmount()) < 0) {
                throw new RuntimeException("Saldo insuficiente para retirar");
            }
            ahorro.setCurrentAmount(ahorro.getCurrentAmount().subtract(transaccion.getAmount()));
        } else {
            ahorro.setCurrentAmount(ahorro.getCurrentAmount().add(transaccion.getAmount()));
        }

        // 3. Guardar el saldo actualizado en la meta
        ahorroRepository.save(ahorro);

        // 4. Guardar el registro en el historial
        transaccion.setAhorro(ahorro);
        transaccion.setDate(LocalDate.now()); // Fecha automática
        return historialRepository.save(transaccion);
    }

    @Override
    public List<AhorroHistorial> listarPorMeta(Long savingId, Long userId) {
        return historialRepository.findByAhorroIdOrderByDateDesc(savingId);
    }
}
