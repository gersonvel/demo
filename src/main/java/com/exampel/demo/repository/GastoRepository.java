package com.exampel.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exampel.demo.dto.GastoResumenDTO;
import com.exampel.demo.model.Gasto;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {

    List<Gasto> findByUserId(Long userId);

    // 1. Para filtrar gastos por un rango de fechas (útil para el balance del mes
    // actual)
    List<Gasto> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);

    // 2. Para la gráfica de pastel (lo que ya tenías, pero con DTO es mejor)
    @Query("SELECT g.category.name AS rubro, SUM(g.amount) AS total " +
            "FROM Gasto g " +
            "WHERE g.user.id = :userId " +
            "GROUP BY g.category.name")
    List<GastoResumenDTO> sumGastosPorCategoria(Long userId);

    @Query("SELECT g.category.name AS rubro, SUM(g.amount) AS total " +
            "FROM Gasto g " +
            "WHERE g.user.id = :userId " +
            "AND MONTH(g.date) = MONTH(CURRENT_DATE) " +
            "AND YEAR(g.date) = YEAR(CURRENT_DATE) " +
            "GROUP BY g.category.name")
    List<GastoResumenDTO> sumGastosPorCategoriaMesActual(Long userId);

    // 3. Para la gráfica de barras (Comportamiento de los últimos 6 meses)
    // Agrupa por mes y año
    @Query("SELECT MONTH(g.date) as mes, YEAR(g.date) as anio, SUM(g.amount) as total FROM Gasto g WHERE g.user.id = :userId GROUP BY YEAR(g.date), MONTH(g.date) ORDER BY YEAR(g.date) DESC, MONTH(g.date) DESC")
    List<Object[]> sumGastosMensuales(Long userId);

    @Query("SELECT g.category.name AS rubro, SUM(g.amount) AS total " +
            "FROM Gasto g " +
            "WHERE g.user.id = :userId " +
            "AND MONTH(g.date) = :mes " +
            "AND YEAR(g.date) = :anio " +
            "GROUP BY g.category.name")
    List<GastoResumenDTO> sumGastosPorMesYAnio(Long userId, int mes, int anio);

    @Query("SELECT g.category.name AS rubro, SUM(g.amount) AS total " +
            "FROM Gasto g " +
            "WHERE g.user.id = :userId " +
            "AND DAY(g.date) = :dia " +
            "AND MONTH(g.date) = :mes " +
            "AND YEAR(g.date) = :anio " +
            "GROUP BY g.category.name")
    List<GastoResumenDTO> sumGastosPorDiaMesYAnio(Long userId, int dia, int mes, int anio);

    // Para Paginación: Trae los gastos de 10 en 10 (o el tamaño que definas)
    Page<Gasto> findByUserIdOrderByDateDesc(Long userId, Pageable pageable);

    // Para Filtro por Fecha Exacta:
    @Query("FROM Gasto g WHERE g.user.id = :userId " +
            "AND DAY(g.date) = :dia " +
            "AND MONTH(g.date) = :mes " +
            "AND YEAR(g.date) = :anio " +
            "ORDER BY g.date DESC")
    List<Gasto> findByUserIdAndDate(Long userId, int dia, int mes, int anio);

    List<Gasto> findByUserIdAndCategoryNameIgnoreCase(Long userId, String categoryName);

    @Query("SELECT g FROM Gasto g WHERE g.user.id = :userId " +
            "AND MONTH(g.date) = :mes " +
            "AND YEAR(g.date) = :anio " +
            "AND (cast(:categoria as string) IS NULL OR LOWER(g.category.name) = LOWER(cast(:categoria as string)))")
    List<Gasto> findByMesAnioYCategoria(
            @Param("userId") Long userId,
            @Param("mes") int mes,
            @Param("anio") int anio,
            @Param("categoria") String categoria);
}
