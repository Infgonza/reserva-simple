package com.rs.reserva_simple.persistance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer duracion;
    private Double precio;
    private String color;
    private Boolean activo;

    // Metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    // Solo ID del negocio
    private Long negocioId;
}
