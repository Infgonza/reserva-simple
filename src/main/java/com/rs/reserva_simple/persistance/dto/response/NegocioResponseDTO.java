package com.rs.reserva_simple.persistance.dto.response;

import com.rs.reserva_simple.persistance.dto.response.basic.ServicioBasicDTO;
import com.rs.reserva_simple.persistance.entity.enums.RolPlataforma;
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
    private String email;
    private RolPlataforma rolPlataforma;

    // DATOS DEL NEGOCIO
    private String nombre;
    private String slug;
    private String direccion;
    private String telefono;
    private String descripcion;
    private String profileImageUrl;
    private Boolean activo;

    // METADATA
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    // RELACIONES

    /**
     * Servicios del negocio
     */
    private List<ServicioBasicDTO> servicios;

    /**
     * IDs de los empleados (UsuarioNegocio)
     */
    private List<Long> empleadosIds;

    /**
     * Contadores de estadísticas
     */
    private Integer totalTurnos;
    private Integer totalClientes;
}
