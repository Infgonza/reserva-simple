package com.rs.reserva_simple.controller;

import com.rs.reserva_simple.persistance.dto.request.ServicioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.ServicioResponseDTO;
import com.rs.reserva_simple.security.CustomUserDetails;
import com.rs.reserva_simple.service.ServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;

    /**
     * Obtener todos los servicios asociados al negocio del usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado.
     * @return Lista de servicios en formato DTO.
     */
    @GetMapping
    public ResponseEntity<List<ServicioResponseDTO>> getAllServices(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        Long negocioId = userDetails.getNegocioId();
        return ResponseEntity.ok(servicioService.findAllByNegocio(negocioId));
    }

    /**
     * Obtener un servicio por su ID, asegurando que pertenece al negocio del usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado.
     * @param id ID del servicio a buscar.
     * @return Servicio en formato DTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponseDTO> findById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        Long negocioId = userDetails.getNegocioId();
        return ResponseEntity.ok(servicioService.findById(id, negocioId));
    }

    /**
     * Crear un nuevo servicio asociado al negocio del usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado.
     * @param dto Datos del servicio a crear.
     * @return Servicio creado en formato DTO.
     */
    @PostMapping
    public ResponseEntity<ServicioResponseDTO> createService(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ServicioRequestDTO dto
    ) {
        Long negocioId = userDetails.getNegocioId();
        ServicioResponseDTO servicio = servicioService.create(negocioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio);
    }

    /**
     * Actualizar un servicio existente, asegurando que pertenece al negocio del usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado.
     * @param id ID del servicio a actualizar.
     * @param dto Datos actualizados del servicio.
     * @return Servicio actualizado en formato DTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServicioResponseDTO> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody ServicioRequestDTO dto
    ) {
        Long negocioId = userDetails.getNegocioId();
        ServicioResponseDTO updated = servicioService.update(id, negocioId, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Eliminar un servicio, asegurando que pertenece al negocio del usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado.
     * @param id ID del servicio a eliminar.
     * @return Respuesta sin contenido.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        Long negocioId = userDetails.getNegocioId();
        servicioService.delete(id, negocioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/public/{negocioSlug}")
    public ResponseEntity<List<ServicioResponseDTO>> findAllBySlug(@PathVariable String negocioSlug) {
        return ResponseEntity.ok(servicioService.findAllBySlug(negocioSlug));
    }

}
