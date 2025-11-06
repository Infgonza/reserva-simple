package com.rs.reserva_simple.mapper;

import com.rs.reserva_simple.persistance.dto.request.NegocioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.NegocioResponseDTO;
import com.rs.reserva_simple.persistance.dto.response.basic.NegocioBasicDTO;
import com.rs.reserva_simple.persistance.entity.Negocio;
import com.rs.reserva_simple.persistance.entity.UsuarioNegocio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidad Negocio y sus DTO
 */
@Mapper(componentModel = "spring", uses = {ServicioMapper.class})
public interface NegocioMapper {

    /**
     * Convierte NegocioRequestDTO a entidad Negocio.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "password", ignore = true) // Se hashea en el Service
    @Mapping(target = "rolPlataforma", ignore = true) // Se asigna automáticamente
    @Mapping(target = "activo", ignore = true) // Valor por defecto en la entidad
    @Mapping(target = "servicios", ignore = true)
    @Mapping(target = "empleados", ignore = true)
    @Mapping(target = "turnos", ignore = true)
    @Mapping(target = "clientes", ignore = true)
    Negocio toEntity(NegocioRequestDTO dto);

    /**
     * Convierte entidad Negocio a NegocioResponseDTO.
     */
    @Mapping(target = "servicios", source = "servicios")
    @Mapping(target = "empleadosIds", source = "empleados", qualifiedByName = "empleadosToIds")
    @Mapping(target = "totalTurnos", expression = "java(entity.getTurnos() != null ? entity.getTurnos().size() : 0)")
    @Mapping(target = "totalClientes", expression = "java(entity.getClientes() != null ? entity.getClientes().size() : 0)")
    NegocioResponseDTO toResponseDTO(Negocio entity);

    /**
     * Convierte entidad Negocio a NegocioBasicDTO
     */
    NegocioBasicDTO toBasicDTO(Negocio entity);

    /**
     * Actualiza una entidad Negocio existente con datos del RequestDTO.
     * La contraseña debe hashearse en el Service si se actualiza.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "password", ignore = true) // Se actualiza manualmente si es necesario
    @Mapping(target = "rolPlataforma", ignore = true) // No se actualiza
    @Mapping(target = "servicios", ignore = true)
    @Mapping(target = "empleados", ignore = true)
    @Mapping(target = "turnos", ignore = true)
    @Mapping(target = "clientes", ignore = true)
    void updateEntityFromDTO(NegocioRequestDTO dto, @MappingTarget Negocio entity);

    /**
     * Método auxiliar para extraer IDs de empleados (UsuarioNegocio).
     */
    @Named("empleadosToIds")
    default List<Long> empleadosToIds(List<UsuarioNegocio> empleados) {
        if (empleados == null) {
            return null;
        }
        return empleados.stream()
                .map(UsuarioNegocio::getId)
                .collect(Collectors.toList());
    }
}
