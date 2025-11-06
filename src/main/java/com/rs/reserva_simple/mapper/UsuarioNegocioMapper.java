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
@Mapper(componentModel = "spring", uses = {NegocioMapper.class})
public interface UsuarioNegocioMapper {


    /**
     * Convierte UsuarioNegocioRequestDTO a entidad UsuarioNegocio.
     * El negocio y el rol se asignan en el Service.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "password", ignore = true) // Se hashea en el Service
    @Mapping(target = "rolNegocio", ignore = true) // Siempre EMPLEADO, se asigna automáticamente
    @Mapping(target = "negocio", ignore = true) // Se asigna en el Service
    @Mapping(target = "turnos", ignore = true)
    UsuarioNegocio toEntity(UsuarioNegocioRequestDTO dto);

    /**
     * Convierte entidad UsuarioNegocio a UsuarioNegocioResponseDTO.
     */
    @Mapping(target = "negocioId", source = "negocio.id")
    UsuarioNegocioResponseDTO toResponseDTO(UsuarioNegocio entity);

    /**
     * Convierte entidad UsuarioNegocio a EmpleadoBasicDTO.
     * Usado en TurnoResponseDTO para mostrar información del empleado.
     */
    @Named("toEmpleadoBasicDTO")
    @Mapping(target = "nombreUsuario", source = "nombre")
    EmpleadoBasicDTO toEmpleadoBasicDTO(UsuarioNegocio entity);

    /**
     * Actualiza una entidad UsuarioNegocio existente con datos del RequestDTO.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "password", ignore = true) // Se actualiza manualmente si es necesario
    @Mapping(target = "rolNegocio", ignore = true) // No se actualiza
    @Mapping(target = "negocio", ignore = true) // No se actualiza
    @Mapping(target = "turnos", ignore = true)
    void updateEntityFromDTO(UsuarioNegocioRequestDTO dto, @MappingTarget UsuarioNegocio entity);

}
