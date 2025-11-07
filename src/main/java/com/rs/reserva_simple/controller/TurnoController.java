package com.rs.reserva_simple.controller;

import com.rs.reserva_simple.persistance.dto.request.TurnoRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.TurnoResponseDTO;
import com.rs.reserva_simple.persistance.dto.response.basic.EmpleadoBasicDTO;
import com.rs.reserva_simple.service.TurnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;


    /**
     * Crea un nuevo turno/reserva
     */
    @PostMapping("/{negocioSlug}")
    public ResponseEntity<TurnoResponseDTO> createTurno(
            @PathVariable String negocioSlug,
            @Valid @RequestBody TurnoRequestDTO requestDTO
    ) {
        TurnoResponseDTO response = turnoService.create(negocioSlug, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene los empleados activos para un servicio específico
     */
    @GetMapping("/empleados/{servicioId}")
    public ResponseEntity<List<EmpleadoBasicDTO>> getEmpleadosByServicio(
            @PathVariable Long servicioId
    ) {
        List<EmpleadoBasicDTO> empleados = turnoService.getActiveEmployeesByService(servicioId);
        return ResponseEntity.ok(empleados);
    }

    /**
     * Obtiene los turnos existentes para una fecha y empleado específicos
     */
    @GetMapping("/turnos")
    public ResponseEntity<List<TurnoResponseDTO>> getTurnosByFechaYEmpleado(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam Long empleadoId
    ) {
        List<TurnoResponseDTO> turnos = turnoService.getTurnosByFechaYEmpleado(fecha, empleadoId);
        // Filtrar solo los turnos que realmente ocupan espacio (no cancelados)
        List<TurnoResponseDTO> turnosActivos = turnos.stream()
                .filter(t -> !t.getEstado().equals("CANCELADO"))
                .toList();
        return ResponseEntity.ok(turnosActivos);
    }
}


