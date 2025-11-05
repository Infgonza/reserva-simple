package com.rs.reserva_simple.persistance.dto.response.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioBasicDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer duracion;
    private Double precio;
    private String color;
    private Boolean activo;
}
