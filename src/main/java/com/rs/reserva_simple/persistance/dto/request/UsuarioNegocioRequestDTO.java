package com.rs.reserva_simple.persistance.dto.request;

import com.rs.reserva_simple.persistance.entity.enums.RolNegocio;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioNegocioRequestDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El ID del negocio es obligatorio")
    private Long negocioId;

    @NotNull(message = "El rol en el negocio es obligatorio")
    private RolNegocio rolNegocio;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
}
