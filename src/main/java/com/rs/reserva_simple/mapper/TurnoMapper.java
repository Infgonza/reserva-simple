package com.rs.reserva_simple.mapper;

import com.rs.reserva_simple.persistance.dto.request.TurnoRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.TurnoResponseDTO;
import com.rs.reserva_simple.persistance.entity.Turno;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper para convertir entre entidad Turno y sus DTOs
 * Incluye otros mappers para convertir objetos anidados a BasicDTOs
 */
@Mapper (componentModel = "spring", uses = {ServicioMapper.class, ClienteMapper.class, UsuarioNegocioMapper.class})
public interface TurnoMapper {

    /**
     * Convierte TurnoRequestDTO a entidad Turno
     * Las relaciones (negocio, servicio, usuarioNegocio, cliente)
     * se cargan manualmente en el Service usando sus IDs
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "negocio", ignore = true)
    @Mapping(target = "servicio", ignore = true)
    @Mapping(target = "usuarioNegocio", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    Turno toEntity(TurnoRequestDTO dto);

    /**
     * Convierte entidad Turno a TurnoResponseDTO
     * Las relaciones se convierten a BasicDTOs automáticamente usando los mappers indicados
     * negocioId se extrae directamente sin usar el mapper
     */
    @Mapping(target = "negocioId", source = "negocio.id")
    @Mapping(target = "servicio", source = "servicio")
    @Mapping(target = "cliente", source = "cliente")
    @Mapping(target = "empleado", source = "usuarioNegocio", qualifiedByName = "toEmpleadoBasicDTO")
    TurnoResponseDTO toResponseDTO(Turno entity);

    /**
     * Actualiza una entidad Turno existente con datos del RequestDTO
     * Las relaciones se actualizan manualmente en el Service si es necesario
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "negocio", ignore = true)
    @Mapping(target = "servicio", ignore = true)
    @Mapping(target = "usuarioNegocio", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    void updateEntityFromDTO(TurnoRequestDTO dto, @MappingTarget Turno entity);

}
