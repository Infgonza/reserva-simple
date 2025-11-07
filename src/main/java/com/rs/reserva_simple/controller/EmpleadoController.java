package com.rs.reserva_simple.controller;

import com.rs.reserva_simple.persistance.dto.request.UsuarioNegocioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.UsuarioNegocioResponseDTO;
import com.rs.reserva_simple.security.CustomUserDetails;
import com.rs.reserva_simple.service.UsuarioNegocioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para gestión de empleados (UsuarioNegocio).
 * Solo accesible por propietarios autenticados.
 */
@RestController
@RequestMapping("/api/personal")
@RequiredArgsConstructor
public class EmpleadoController {


    private final UsuarioNegocioService empleadoService;

    /**
     * Obtiene todos los empleados del negocio autenticado.
     */
    @GetMapping
    public ResponseEntity<List<UsuarioNegocioResponseDTO>> getAllPersonal(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long negocioId = userDetails.getNegocioId();
        return ResponseEntity.ok(empleadoService.findAllByNegocio(negocioId));
    }

    /**
     * Obtiene un empleado específico por ID.
     * Verifica que pertenezca al negocio autenticado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioNegocioResponseDTO> findById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        Long negocioId = userDetails.getNegocioId();
        return ResponseEntity.ok(empleadoService.findById(id, negocioId));
    }

    /**
     * Crea un nuevo empleado para el negocio autenticado.
     */
    @PostMapping
    public ResponseEntity<UsuarioNegocioResponseDTO> createPersonal(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UsuarioNegocioRequestDTO dto
    ) {
        Long negocioId = userDetails.getNegocioId();
        UsuarioNegocioResponseDTO empleado = empleadoService.create(negocioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(empleado);
    }

    /**
     * Actualiza un empleado existente.
     * Verifica que pertenezca al negocio autenticado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioNegocioResponseDTO> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UsuarioNegocioRequestDTO dto
    ) {
        Long negocioId = userDetails.getNegocioId();
        UsuarioNegocioResponseDTO updated = empleadoService.update(id, negocioId, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Elimina (desactiva) un empleado.
     * Verifica que pertenezca al negocio autenticado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonal(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        Long negocioId = userDetails.getNegocioId();
        empleadoService.delete(id, negocioId);
        return ResponseEntity.noContent().build();
    }
}
