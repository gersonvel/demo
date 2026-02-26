package com.exampel.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exampel.demo.model.Ahorro;

@Repository
public interface AhorroRepository extends JpaRepository<Ahorro, Long> {
    List<Ahorro> findByUserId(Long userId);
}