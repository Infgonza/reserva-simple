package com.rs.reserva_simple.controller;

import com.rs.reserva_simple.persistance.dto.response.TurnoResponseDTO;
import com.rs.reserva_simple.security.CustomUserDetails;
import com.rs.reserva_simple.service.TurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;

    /**
     * Obtiene un turno por su ID, asegurando que pertenece al negocio del usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado.
     * @param id ID del turno a buscar.
     * @return Turno en formato DTO.
     */
    @GetMapping("{id}")
    public ResponseEntity<TurnoResponseDTO> getById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ){

        Long negocioId = userDetails.getNegocioId();
        return ResponseEntity.ok(turnoService.findById(id, negocioId));
    }

    /**
     * Obtiene todos los turnos asociados al negocio del usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado.
     * @return Lista de turnos en formato DTO.
     */
    @GetMapping
    public ResponseEntity<List<TurnoResponseDTO>> getAllAppointments(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Long negocioId = userDetails.getNegocioId();
        return ResponseEntity.ok(turnoService.findAll(negocioId));
    }

    /**
     * Eliminar un turno, asegurando que pertenece al negocio del usuario autenticado.
     *
     * @param customUserDetails Detalles del usuario autenticado.
     * @param id ID del turno a eliminar.
     * @return Respuesta sin contenido.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long id
    ){
        Long negocioId = customUserDetails.getNegocioId();
        turnoService.delete(id, negocioId);
        return ResponseEntity.noContent().build();
    }


}
