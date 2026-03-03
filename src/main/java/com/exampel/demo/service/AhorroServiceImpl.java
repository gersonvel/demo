package com.exampel.demo.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exampel.demo.model.Ahorro;
import com.exampel.demo.model.Usuario;
import com.exampel.demo.repository.AhorroRepository;

@Service
public class AhorroServiceImpl implements AhorroService {

    @Autowired
    private AhorroRepository repository;

    @Override
    public List<Ahorro> listarPorUsuario(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public Ahorro guardar(Ahorro ahorro, Long userId) {
        Usuario user = new Usuario();
        user.setId(userId);
        ahorro.setUser(user);
        if (ahorro.getCurrentAmount() == null)
            ahorro.setCurrentAmount(BigDecimal.ZERO);
        return repository.save(ahorro);
    }

    @Override
    public Ahorro actualizarSaldo(Long id, BigDecimal montoNuevo, Long userId) {
        Ahorro existente = repository.findById(id)
                .filter(a -> a.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Meta de ahorro no encontrada"));

        existente.setCurrentAmount(montoNuevo);
        return repository.save(existente);
    }

    @Override
    public void eliminar(Long id, Long userId) {
        Ahorro ahorro = repository.findById(id)
                .filter(a -> a.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("No encontrado"));
        repository.delete(ahorro);
    }

    @Override
    public Ahorro actualizarMetaCompleta(Long id, Ahorro datosNuevos, Long userId) {
        // 1. Buscamos la meta existente asegurándonos que pertenezca al usuario
        Ahorro ahorro = repository.findById(id)
                .filter(a -> a.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Meta de ahorro no encontrada o no autorizada"));

        // 2. Actualizamos solo los campos descriptivos y la meta final
        ahorro.setName(datosNuevos.getName());
        ahorro.setTargetAmount(datosNuevos.getTargetAmount());
        ahorro.setIcon(datosNuevos.getIcon());
        ahorro.setColor(datosNuevos.getColor());
        ahorro.setDeadline(datosNuevos.getDeadline());

        // Nota: El currentAmount (lo ahorrado) normalmente se mantiene igual
        // a menos que también quieras permitir editarlo desde el modal principal.
        // ahorro.setCurrentAmount(datosNuevos.getCurrentAmount());

        // 3. Guardamos los cambios
        return repository.save(ahorro);
    }
}
