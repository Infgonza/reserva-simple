package com.rs.reserva_simple.persistance.dto.response;

import com.rs.reserva_simple.persistance.dto.response.basic.ClienteBasicDTO;
import com.rs.reserva_simple.persistance.dto.response.basic.EmpleadoBasicDTO;
import com.rs.reserva_simple.persistance.dto.response.basic.ServicioBasicDTO;
import com.rs.reserva_simple.persistance.entity.enums.Estado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnoResponseDTO {
    private Long id;
    private LocalDate fechaTurno;
    private OffsetDateTime horaInicio;
    private OffsetDateTime horaFinal;
    private Estado estado;
    private String notas;

    // Metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    // Relaciones como objetos básicos para visualización
    private Long negocioId;
    private ServicioBasicDTO servicio;
    private ClienteBasicDTO cliente;
    private EmpleadoBasicDTO empleado;
}
