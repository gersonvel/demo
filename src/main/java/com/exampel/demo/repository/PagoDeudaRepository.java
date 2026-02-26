package com.exampel.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exampel.demo.model.PagoDeuda;

@Repository
public interface PagoDeudaRepository extends JpaRepository<PagoDeuda, Long> {
    List<PagoDeuda> findByDeudaIdOrderByDateDesc(Long deudaId);
}
