package com.rs.reserva_simple.persistance.dto.response;

import com.rs.reserva_simple.persistance.entity.enums.RolPlataforma;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String email;
    private String nombre;
    private String telefono;
    private RolPlataforma rolPlataforma;

    // Metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    // Solo IDs de relaciones para evitar recursión
    private List<Long> negociosIds;

}
