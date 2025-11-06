package com.rs.reserva_simple.mapper;

import com.rs.reserva_simple.persistance.dto.request.ServicioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.ServicioResponseDTO;
import com.rs.reserva_simple.persistance.dto.response.basic.ServicioBasicDTO;
import com.rs.reserva_simple.persistance.entity.Servicio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper para convertir entre entidad Servicio y sus DTO
 */
@Mapper(componentModel = "spring")
public interface ServicioMapper {
    /**
     * Convierte ServicioRequestDTO a entidad Servicio
     * negocioId se mapea manualmente en el Service cargando el Negocio
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "negocio", ignore = true)
    @Mapping(target = "turnos", ignore = true)
    Servicio toEntity(ServicioRequestDTO dto);

    /**
     * Convierte la entidad Servicio a ServicioResponseDTO
     * negocioId se extrae directamente del negocio asociado
     */
    @Mapping(target = "negocioId", source = "negocio.id")
    ServicioResponseDTO toResponseDTO(Servicio entity);

    /**
     * Convierte entidad Servicio a ServicioBasicDTO
     */
    ServicioBasicDTO toBasicDTO(Servicio entity);

    /**
     * Actualiza una entidad Servicio existente con datos del RequestDTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "negocio", ignore = true)
    @Mapping(target = "turnos", ignore = true)
    void updateEntityFromDTO(ServicioRequestDTO dto, @MappingTarget Servicio entity);

}
