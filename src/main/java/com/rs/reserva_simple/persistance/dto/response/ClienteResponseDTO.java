package com.rs.reserva_simple.persistance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponseDTO {
    private Long id;
    private String nombre;
    private String telefono;
    private String email;
    private String notas;

    // Estadísticas del cliente
    private Integer cantidadTurnos;
    private Instant fechaPrimerTurno;
    private Instant fechaUltimoTurno;

    // Metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    // Solo ID del negocio
    private Long negocioId;
}
