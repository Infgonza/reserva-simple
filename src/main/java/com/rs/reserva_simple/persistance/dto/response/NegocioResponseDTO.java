package com.rs.reserva_simple.persistance.dto.response;

import com.rs.reserva_simple.persistance.dto.response.basic.ServicioBasicDTO;
import com.rs.reserva_simple.persistance.dto.response.basic.UsuarioBasicDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NegocioResponseDTO{

    private Long id;
    private String nombre;
    private String slug;
    private String direccion;
    private String telefono;
    private String email;
    private Boolean activo;

    // Metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    // Propietario como objeto anidado básico
    private UsuarioBasicDTO propietario;

    // Servicios completos (lista pequeña, información crítica)
    private List<ServicioBasicDTO> servicios;

    // Solo IDs para colecciones grandes
    private List<Long> empleadosIds;
    private Integer totalTurnos;
    private Integer totalClientes;
}
