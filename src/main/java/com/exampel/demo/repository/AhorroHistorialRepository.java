package com.exampel.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exampel.demo.model.AhorroHistorial;

@Repository
public interface AhorroHistorialRepository extends JpaRepository<AhorroHistorial, Long> {
    // Para ver los movimientos de una meta específica
    List<AhorroHistorial> findByAhorroIdOrderByDateDesc(Long savingId);

    // Para ver todos los movimientos del usuario (opcional para el dashboard)
    List<AhorroHistorial> findByAhorroUserIdOrderByDateDesc(Long userId);
}