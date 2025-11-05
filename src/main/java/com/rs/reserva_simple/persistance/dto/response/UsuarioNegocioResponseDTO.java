package com.rs.reserva_simple.persistance.dto.response;

import com.rs.reserva_simple.persistance.dto.response.basic.NegocioBasicDTO;
import com.rs.reserva_simple.persistance.dto.response.basic.UsuarioBasicDTO;
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
    private RolNegocio rolNegocio;
    private Boolean activo;

    // Metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    // Información básica del usuario
    private UsuarioBasicDTO usuario;

    // Información básica del negocio
    private NegocioBasicDTO negocio;
}
