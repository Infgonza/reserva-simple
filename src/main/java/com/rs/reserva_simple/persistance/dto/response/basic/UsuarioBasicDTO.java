package com.rs.reserva_simple.persistance.dto.response.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioBasicDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
}
