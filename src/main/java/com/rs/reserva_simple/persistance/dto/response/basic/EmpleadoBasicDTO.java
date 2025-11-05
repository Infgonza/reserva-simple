package com.rs.reserva_simple.persistance.dto.response.basic;

import com.rs.reserva_simple.persistance.entity.enums.RolNegocio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoBasicDTO {
    private Long id;
    private String nombreUsuario;
    private RolNegocio rolNegocio;
    private Boolean activo;
}
