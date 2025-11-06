package com.rs.reserva_simple.mapper;

import com.rs.reserva_simple.persistance.dto.request.UsuarioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.UsuarioResponseDTO;
import com.rs.reserva_simple.persistance.dto.response.basic.UsuarioBasicDTO;
import com.rs.reserva_simple.persistance.entity.Negocio;
import com.rs.reserva_simple.persistance.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidad Usuario y sus DTO
 *
 */
@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    /**
     * Convierte UsuarioRequestDTO a entidad Usuario
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "negocios", ignore = true)
    @Mapping(target = "trabaja", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);

    /**
     * Convierte entidad Usuario a UsuarioResponseDTO
     * negociosIds se mapea extrayendo los IDs de la colección de negocios
     */
    @Mapping(target = "negociosIds",  expression = "java(getNegociosIds(entity))")
    UsuarioResponseDTO toResponseDTO(Usuario entity);

    /**
     * Convierte entidad Usuario a UsuarioBasicDTO
     */
    UsuarioBasicDTO toBasicDTO(Usuario entity);

    /**
     * Actualiza una entidad Usuario existente con datos del RequestDTO
     * Se ignoran campos que no deben ser actualizados por el usuario
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "negocios", ignore = true)
    @Mapping(target = "trabaja", ignore = true)
    void updateEntityFromDTO(UsuarioRequestDTO dto, @MappingTarget Usuario entity);

    /**
     * Método auxiliar para extraer IDs de una lista de Negocios
     */
    default List<Long> getNegociosIds(Usuario entity) {
        return entity.getNegocios().stream()
                .map(Negocio::getId)
                .collect(Collectors.toList());
    }
}
