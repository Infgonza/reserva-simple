package com.rs.reserva_simple.controller;

import com.rs.reserva_simple.persistance.dto.request.NegocioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.NegocioResponseDTO;
import com.rs.reserva_simple.security.CustomUserDetails;
import com.rs.reserva_simple.service.NegocioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/negocios")
public class NegocioController {


    private final NegocioService negocioService;

    /**
     * Obtiene todos los negocios
     */
    @GetMapping
    public ResponseEntity<List<NegocioResponseDTO>> findAll() {
        return ResponseEntity.ok(negocioService.findAll());
    }

    /**
     * Obtiene un negocio por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<NegocioResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(negocioService.findById(id));
    }

    /**
     * Obtiene el perfil del negocio autenticado.
     * Usa @AuthenticationPrincipal para obtener el negocio del token JWT.
     */
    @GetMapping("/me")
    public ResponseEntity<NegocioResponseDTO> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long negocioId = userDetails.getNegocioId();
        return ResponseEntity.ok(negocioService.findById(negocioId));
    }

    /**
     * Actualiza el perfil del negocio autenticado.
     * Solo el propietario puede actualizar su propio negocio.
     */
    @PutMapping("/me")
    public ResponseEntity<NegocioResponseDTO> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody NegocioRequestDTO dto
    ) {
        Long negocioId = userDetails.getNegocioId();
        NegocioResponseDTO updated = negocioService.update(negocioId, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Desactiva el negocio autenticado (borrado lógico).
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long negocioId = userDetails.getNegocioId();
        negocioService.delete(negocioId);
        return ResponseEntity.noContent().build();
    }
}
