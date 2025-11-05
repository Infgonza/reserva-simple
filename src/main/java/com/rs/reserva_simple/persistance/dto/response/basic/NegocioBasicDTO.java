package com.rs.reserva_simple.persistance.dto.response.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NegocioBasicDTO {
    private Long id;
    private String nombre;
    private String slug;
    private String direccion;
    private String telefono;
    private Boolean activo;
}
