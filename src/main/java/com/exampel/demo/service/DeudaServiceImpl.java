package com.exampel.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exampel.demo.model.Categoria;
import com.exampel.demo.model.Deuda;
import com.exampel.demo.model.Gasto;
import com.exampel.demo.model.PagoDeuda;
import com.exampel.demo.model.Usuario;
import com.exampel.demo.repository.CategoriaRepository;
import com.exampel.demo.repository.DeudaRepository;
import com.exampel.demo.repository.GastoRepository;
import com.exampel.demo.repository.PagoDeudaRepository;

import jakarta.transaction.Transactional;

@Service
public class DeudaServiceImpl implements DeudaService {

    @Autowired
    private DeudaRepository deudaRepository;

    @Autowired
    private PagoDeudaRepository pagoDeudaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private GastoRepository gastoRepository;

    @Override
    public List<Deuda> listarPorUsuario(Long userId) {
        return deudaRepository.findByUserId(userId);
    }

    @Override
    public Deuda obtenerPorId(Long id, Long userId) {
        return deudaRepository.findById(id)
                .filter(d -> d.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada o no tienes permisos para verla"));
    }

    @Override
    public Deuda guardar(Deuda deuda, Long userId) {
        Usuario user = new Usuario();
        user.setId(userId);
        deuda.setUser(user);

        // Si el usuario no define un balance actual, asumimos que es el total
        if (deuda.getBalance() == null) {
            deuda.setBalance(deuda.getTotalAmount());
        }

        return deudaRepository.save(deuda);
    }

    @Override
    public void eliminar(Long id, Long userId) {
        Deuda deuda = deudaRepository.findById(id)
                .filter(d -> d.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));
        deudaRepository.delete(deuda);
    }

    // @Override
    // public Deuda actualizar(Long id, Deuda detalles, Long userId) {
    // Deuda existente = deudaRepository.findById(id)
    // .filter(d -> d.getUser().getId().equals(userId))
    // .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));

    // existente.setName(detalles.getName());
    // existente.setTotalAmount(detalles.getTotalAmount());
    // existente.setBalance(detalles.getBalance());
    // existente.setMonthlyPayment(detalles.getMonthlyPayment());
    // existente.setDueDateDay(detalles.getDueDateDay());

    // return deudaRepository.save(existente);
    // }

    @Override
    @Transactional
    public Deuda actualizar(Long id, Deuda detalles, Long userId) {
        Deuda existente = deudaRepository.findById(id)
                .filter(d -> d.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));

        // 1. Lógica de ajuste de Balance si el TotalAmount cambió
        // Si el total que viene del front es distinto al que tenemos en BD...
        if (detalles.getTotalAmount() != null &&
                detalles.getTotalAmount().compareTo(existente.getTotalAmount()) != 0) {

            // Calculamos lo que el usuario ya pagó (Histórico)
            BigDecimal pagadoHastaAhora = existente.getTotalAmount().subtract(existente.getBalance());

            // El nuevo balance es el nuevo total menos lo que ya pagó
            BigDecimal nuevoBalance = detalles.getTotalAmount().subtract(pagadoHastaAhora);

            // Validación: No puede poner un total menor a lo que ya pagó
            if (nuevoBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException(
                        "No puedes reducir el total por debajo de lo ya pagado ($" + pagadoHastaAhora + ")");
            }

            existente.setTotalAmount(detalles.getTotalAmount());
            existente.setBalance(nuevoBalance);
        }

        // 2. Actualizar el resto de campos normales
        existente.setName(detalles.getName());
        existente.setMonthlyPayment(detalles.getMonthlyPayment());
        existente.setDueDateDay(detalles.getDueDateDay());
        // El startDate y otros campos si los necesitas

        return deudaRepository.save(existente);
    }

    @Transactional
    public PagoDeuda registrarPago(Long deudaId, BigDecimal monto, String nota, Long userId) {
        // 1. Validaciones de Deuda (lo que ya teníamos)
        Deuda deuda = deudaRepository.findById(deudaId)
                .filter(d -> d.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));

        // 2. BUSCAR O CREAR CATEGORÍA "PAGO DE DEUDA" PARA ESTE USUARIO
        Categoria catDeuda = categoriaRepository.findByUserId(userId).stream()
                .filter(c -> c.getName().equalsIgnoreCase("Pago de Deuda"))
                .findFirst()
                .orElseGet(() -> {
                    // Si no existe, la creamos de una vez
                    Categoria nuevaCat = new Categoria();
                    nuevaCat.setName("Pago de Deuda");
                    nuevaCat.setIcon("💸"); // Icono por defecto
                    nuevaCat.setColor("#FF4500"); // Color distintivo
                    nuevaCat.setUser(deuda.getUser());
                    return categoriaRepository.save(nuevaCat);
                });

        // 3. Actualizar Saldo de la Deuda
        deuda.setBalance(deuda.getBalance().subtract(monto));
        deudaRepository.save(deuda);

        // 4. Crear el Registro de Pago (Historial de Deuda)
        PagoDeuda pago = new PagoDeuda();
        pago.setAmount(monto);
        pago.setDate(LocalDate.now());
        pago.setNote(nota);
        pago.setDeuda(deuda);
        PagoDeuda pagoGuardado = pagoDeudaRepository.save(pago);

        // 5. REGISTRAR COMO GASTO AUTOMÁTICAMENTE
        Gasto gastoDeuda = new Gasto();
        gastoDeuda.setAmount(monto);
        gastoDeuda.setDate(LocalDate.now());
        gastoDeuda.setDescription("Abono a: " + deuda.getName() + " - " + nota);
        gastoDeuda.setUser(deuda.getUser());
        gastoDeuda.setCategory(catDeuda); // Usamos la categoría que encontramos o creamos

        gastoRepository.save(gastoDeuda);

        return pagoGuardado;
    }

    @Override
    public List<PagoDeuda> listarPagosPorDeuda(Long deudaId, Long userId) {
        // Validamos que la deuda pertenezca al usuario antes de mostrar el historial
        Deuda deuda = deudaRepository.findById(deudaId)
                .filter(d -> d.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada o no autorizada"));

        return pagoDeudaRepository.findByDeudaIdOrderByDateDesc(deudaId);
    }

}
