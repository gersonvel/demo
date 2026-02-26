package com.exampel.demo.controller;

import com.exampel.demo.dto.ResponseDTO;
import com.exampel.demo.model.Categoria;
import com.exampel.demo.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
// @CrossOrigin(origins = "http://localhost:3000")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<ResponseDTO> listarPorUsuario(@PathVariable Long userId) {
        List<Categoria> categorias = categoriaService.listarPorUsuario(userId);
        return ResponseEntity.ok(new ResponseDTO(
                HttpStatus.OK.value(),
                false,
                "Categorías obtenidas con éxito",
                categorias));
    }

    @PostMapping("/usuario/{userId}")
    public ResponseEntity<ResponseDTO> guardar(@RequestBody Categoria categoria, @PathVariable Long userId) {
        try {
            Categoria nuevaCategoria = categoriaService.guardar(categoria, userId);
            ResponseDTO success = new ResponseDTO(200, false, "Categoría creada correctamente", nuevaCategoria);
            return ResponseEntity.ok(success);

        } catch (Exception e) {

            ResponseDTO error = new ResponseDTO(400, true, e.getMessage(), null);
            return ResponseEntity.badRequest().body(error);

        }
    }

    @GetMapping("/{id}/usuario/{userId}")
    public ResponseEntity<ResponseDTO> obtenerPorId(@PathVariable Long id, @PathVariable Long userId) {
        try {
            Categoria categoria = categoriaService.buscarPorId(id, userId);
            return ResponseEntity.ok(new ResponseDTO(
                    HttpStatus.OK.value(),
                    false,
                    "Categoría encontrada",
                    categoria));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                    HttpStatus.NOT_FOUND.value(),
                    true,
                    e.getMessage(),
                    null));
        }
    }

    @DeleteMapping("/{id}/usuario/{userId}")
    public ResponseEntity<ResponseDTO> eliminar(@PathVariable Long id, @PathVariable Long userId) {
        try {
            categoriaService.eliminar(id, userId);
            return ResponseEntity.ok(new ResponseDTO(
                    HttpStatus.OK.value(),
                    false,
                    "Categoría eliminada con éxito",
                    null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDTO(
                    HttpStatus.FORBIDDEN.value(),
                    true,
                    "No tienes permiso para eliminar esta categoría",
                    null));
        }
    }

    @PostMapping("/{id}/usuario/{userId}")
    public ResponseEntity<ResponseDTO> actualizar(
            @PathVariable Long id,
            @PathVariable Long userId,
            @RequestBody Categoria categoria) {
        try {
            Categoria actualizada = categoriaService.actualizar(id, categoria, userId);
            return ResponseEntity.ok(new ResponseDTO(
                    HttpStatus.OK.value(),
                    false,
                    "Categoría actualizada correctamente",
                    actualizada));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                    HttpStatus.NOT_FOUND.value(),
                    true,
                    e.getMessage(),
                    null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    true,
                    "Error al actualizar la categoría",
                    null));
        }
    }
}