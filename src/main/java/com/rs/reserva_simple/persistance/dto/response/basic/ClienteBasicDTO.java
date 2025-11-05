package com.rs.reserva_simple.persistance.dto.response.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteBasicDTO {
    private Long id;
    private String nombre;
    private String telefono;
    private String email;
}
