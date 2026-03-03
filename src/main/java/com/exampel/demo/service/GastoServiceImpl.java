package com.exampel.demo.service;

import com.exampel.demo.dto.GastoResumenDTO;
import com.exampel.demo.model.Gasto;
import com.exampel.demo.model.Usuario;
import com.exampel.demo.repository.GastoRepository;
import com.exampel.demo.service.CategoriaService;
import com.exampel.demo.service.GastoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GastoServiceImpl implements GastoService {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private CategoriaService categoriaService; // Para validar la categoría

    // @Override
    // @Transactional(readOnly = true)
    // public List<Gasto> listarPorUsuario(Long userId) {
    // return gastoRepository.findByUserId(userId);
    // }

    @Override
    @Transactional(readOnly = true)
    public List<Gasto> listarPorUsuario(Long userId, String categoria) {
        if (categoria != null && !categoria.isEmpty()) {
            // Buscamos por usuario Y nombre de categoría
            return gastoRepository.findByUserIdAndCategoryNameIgnoreCase(userId, categoria);
        }
        // Si no hay categoría, devolvemos todo como antes
        return gastoRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Gasto obtenerPorId(Long id, Long userId) {
        return gastoRepository.findById(id)
                .filter(g -> g.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Gasto no encontrado o no tienes permisos para verlo"));
    }

    @Override
    @Transactional
    public Gasto guardar(Gasto gasto, Long userId) {
        // 1. Validar que la categoría que viene en el JSON exista y sea del usuario
        categoriaService.buscarPorId(gasto.getCategory().getId(), userId);

        // 2. Asignar el usuario al gasto
        Usuario user = new Usuario();
        user.setId(userId);
        gasto.setUser(user);

        return gastoRepository.save(gasto);
    }

    @Override
    @Transactional
    public void eliminar(Long id, Long userId) {
        Gasto existente = gastoRepository.findById(id)
                .filter(g -> g.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Gasto no encontrado o acceso denegado"));
        gastoRepository.delete(existente);
    }

    @Override
    @Transactional
    public Gasto actualizar(Long id, Gasto detallesGasto, Long userId) {
        // 1. Verificar que el gasto exista y pertenezca al usuario
        Gasto gastoExistente = gastoRepository.findById(id)
                .filter(g -> g.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Gasto no encontrado"));

        // 2. Validar que la nueva categoría (si se cambió) sea válida
        categoriaService.buscarPorId(detallesGasto.getCategory().getId(), userId);

        // 3. Actualizar campos
        gastoExistente.setDescription(detallesGasto.getDescription());
        gastoExistente.setAmount(detallesGasto.getAmount());
        gastoExistente.setDate(detallesGasto.getDate());
        gastoExistente.setCategory(detallesGasto.getCategory());

        return gastoRepository.save(gastoExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GastoResumenDTO> resumenPorCategoria(Long userId) {
        return gastoRepository.sumGastosPorCategoria(userId);
    }

    @Override
    public Page<Gasto> listarPaginado(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return gastoRepository.findByUserIdOrderByDateDesc(userId, pageable);
    }

    @Override
    public List<Gasto> listarPorFechaExacta(Long userId, int dia, int mes, int anio) {
        return gastoRepository.findByUserIdAndDate(userId, dia, mes, anio);
    }

    @Override
    public List<Gasto> listarPorMes(Long userId, int mes, int anio, String categoria) {
        // Si la categoría viene vacía desde el Front, la tratamos como null para el
        // Query
        String catFiltro = (categoria != null && !categoria.isEmpty()) ? categoria : null;
        return gastoRepository.findByMesAnioYCategoria(userId, mes, anio, catFiltro);
    }
}