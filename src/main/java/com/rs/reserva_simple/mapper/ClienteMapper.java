package com.rs.reserva_simple.mapper;

import com.rs.reserva_simple.persistance.dto.request.ClienteRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.ClienteResponseDTO;
import com.rs.reserva_simple.persistance.dto.response.basic.ClienteBasicDTO;
import com.rs.reserva_simple.persistance.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper para convertir entre entidad Cliente y sus DTO
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper {

    /**
     * Convierte ClienteRequestDTO a entidad Cliente
     * negocioId se mapea manualmente en el Service cargando el Negocio
     * Los campos de estadísticas (cantidadTurnos, fechaPrimerTurno, fechaUltimoTurno)
     * se gestionan automáticamente en el Service
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "negocio", ignore = true)
    @Mapping(target = "turnos", ignore = true)
    @Mapping(target = "cantidadTurnos", ignore = true)
    @Mapping(target = "fechaPrimerTurno", ignore = true)
    @Mapping(target = "fechaUltimoTurno", ignore = true)
    Cliente toEntity(ClienteRequestDTO dto);

    /**
     * Convierte entidad Cliente a ClienteResponseDTO
     * negocioId se extrae directamente del negocio asociado
     */
    @Mapping(target = "negocioId", source = "negocio.id")
    ClienteResponseDTO toResponseDTO(Cliente entity);

    /**
     * Convierte entidad Cliente a ClienteBasicDTO
     * Usado en TurnoResponseDTO para mostrar información básica del cliente
     */
    ClienteBasicDTO toBasicDTO(Cliente entity);

    /**
     * Actualiza una entidad Cliente existente con datos del RequestDTO
     * No se actualizan los campos de estadísticas, se gestionan automáticamente
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "negocio", ignore = true)
    @Mapping(target = "turnos", ignore = true)
    @Mapping(target = "cantidadTurnos", ignore = true)
    @Mapping(target = "fechaPrimerTurno", ignore = true)
    @Mapping(target = "fechaUltimoTurno", ignore = true)
    void updateEntityFromDTO(ClienteRequestDTO dto, @MappingTarget Cliente entity);

}
