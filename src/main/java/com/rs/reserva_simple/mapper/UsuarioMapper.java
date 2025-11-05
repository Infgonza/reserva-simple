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

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    // Request DTO → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "negocios", ignore = true)
    @Mapping(target = "trabaja", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);

    // Entity → Response DTO
    @Mapping(target = "negociosIds", expression = "java(getNegociosIds(entity))")
    UsuarioResponseDTO toResponseDTO(Usuario entity);

    // Entity → Basic DTO
    UsuarioBasicDTO toBasicDTO(Usuario entity);

    // Update entity from DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "negocios", ignore = true)
    @Mapping(target = "trabaja", ignore = true)
    void updateEntityFromDTO(UsuarioRequestDTO dto, @MappingTarget Usuario entity);

    // Helper method
    default List<Long> getNegociosIds(Usuario entity) {
        return entity.getNegocios().stream()
                .map(Negocio::getId)
                .collect(Collectors.toList());
    }
}
