package com.rs.reserva_simple.persistance.dto.response;

import com.rs.reserva_simple.persistance.entity.enums.RolNegocio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioNegocioResponseDTO {

    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private RolNegocio rolNegocio; // Siempre EMPLEADO
    private Boolean activo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;


    private Long negocioId;
}
