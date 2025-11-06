package com.rs.reserva_simple.mapper;

import com.rs.reserva_simple.persistance.dto.request.UsuarioNegocioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.UsuarioNegocioResponseDTO;
import com.rs.reserva_simple.persistance.dto.response.basic.EmpleadoBasicDTO;
import com.rs.reserva_simple.persistance.entity.UsuarioNegocio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

/**
 * Mapper para convertir entre entidad UsuarioNegocio y sus DTOs
 */
@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, NegocioMapper.class})
public interface UsuarioNegocioMapper {

    /**
     * Convierte UsuarioNegocioRequestDTO a entidad UsuarioNegocio
     * Las relaciones (usuario, negocio) se cargan manualmente en el Service
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "negocio", ignore = true)
    @Mapping(target = "turnos", ignore = true)
    UsuarioNegocio toEntity(UsuarioNegocioRequestDTO dto);

    /**
     * Convierte entidad UsuarioNegocio a UsuarioNegocioResponseDTO
     */
    @Mapping(target = "usuario", source = "usuario")
    @Mapping(target = "negocio", source = "negocio")
    UsuarioNegocioResponseDTO toResponseDTO(UsuarioNegocio entity);

    /**
     * Convierte entidad UsuarioNegocio a EmpleadoBasicDTO
     * Este DTO se usa en TurnoResponseDTO para mostrar información del empleado
     */
    @Named("toEmpleadoBasicDTO")
    @Mapping(target = "nombreUsuario", source = "usuario.nombre")
    EmpleadoBasicDTO toEmpleadoBasicDTO(UsuarioNegocio entity);

    /**
     * Actualiza una entidad UsuarioNegocio existente con datos del RequestDTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "negocio", ignore = true)
    @Mapping(target = "turnos", ignore = true)
    void updateEntityFromDTO(UsuarioNegocioRequestDTO dto, @MappingTarget UsuarioNegocio entity);

}
