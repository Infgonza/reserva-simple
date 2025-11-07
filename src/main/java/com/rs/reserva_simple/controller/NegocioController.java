package com.rs.reserva_simple.controller;

import com.rs.reserva_simple.persistance.dto.request.NegocioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.NegocioResponseDTO;
import com.rs.reserva_simple.security.CustomUserDetails;
import com.rs.reserva_simple.service.NegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/negocio")
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
     * Obtiene el perfil/Negocio del usuario autenticado.
     */
    @GetMapping("/profile")
    public NegocioResponseDTO getProfile() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new RuntimeException("Usuario no autenticado.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long usuarioNegocioId = userDetails.getNegocioId();


        return negocioService.findById(usuarioNegocioId);
    }

    /**
     * Actualiza el perfil/Negocio del usuario autenticado.
     */
    @PutMapping("/profile")
    public NegocioResponseDTO updateProfile(@RequestBody NegocioRequestDTO negocioRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long negocioId = userDetails.getNegocioId();

        return negocioService.updateNegocioProfile(negocioId, negocioRequest);
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
