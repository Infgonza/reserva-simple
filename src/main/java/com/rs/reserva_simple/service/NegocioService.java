package com.rs.reserva_simple.service;


import com.rs.reserva_simple.mapper.NegocioMapper;
import com.rs.reserva_simple.persistance.dto.request.NegocioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.NegocioResponseDTO;
import com.rs.reserva_simple.persistance.entity.Negocio;
import com.rs.reserva_simple.persistance.entity.enums.RolPlataforma;
import com.rs.reserva_simple.persistance.repository.NegocioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para operaciones CRUD de Negocios.
 * Maneja la lógica de negocio, validaciones y seguridad.
 */
@Service
@RequiredArgsConstructor
public class NegocioService {

    private final NegocioRepository negocioRepository;
    private final NegocioMapper negocioMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Busca todos los negocios (admin)
     */
    @Transactional(readOnly = true)
    public List<NegocioResponseDTO> findAll() {
        List<Negocio> negocios = negocioRepository.findAll();
        return negocios.stream()
                .map(negocioMapper::toResponseDTO)
                .toList();
    }

    /**
     * Busca un negocio por ID
     *
     * @param id ID del negocio
     * @return NegocioResponseDTO
     * @throws EntityNotFoundException si no se encuentra el negocio
     */
    @Transactional(readOnly = true)
    public NegocioResponseDTO findById(Long id) {
        Negocio negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Negocio no encontrado con id: " + id
                ));
        return negocioMapper.toResponseDTO(negocio);
    }

    /**
     * Crea un nuevo negocio (registro).
     * Asigna automáticamente el rol PROPIETARIO y hashea la contraseña.
     *
     * @param dto Datos del negocio a crear
     * @return NegocioResponseDTO del negocio creado
     * @throws IllegalArgumentException si el email ya está registrado
     */
    @Transactional
    public NegocioResponseDTO create(NegocioRequestDTO dto) {
        if (negocioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe un negocio registrado con el email: " + dto.getEmail()
            );
        }

        if (negocioRepository.findBySlug(dto.getSlug()).isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe un negocio con el slug: " + dto.getSlug()
            );
        }

        Negocio negocio = negocioMapper.toEntity(dto);

        negocio.setPassword(passwordEncoder.encode(dto.getPassword()));

        negocio.setRolPlataforma(RolPlataforma.USUARIO);

        negocio.setActivo(true);

        Negocio savedNegocio = negocioRepository.save(negocio);

        return negocioMapper.toResponseDTO(savedNegocio);
    }

    /**
     * Actualiza un negocio existente
     * Solo actualiza campos permitidos (no cambia email ni contraseña)
     *
     * @param id ID del negocio a actualizar
     * @param dto Datos actualizados
     * @return NegocioResponseDTO del negocio actualizado
     * @throws EntityNotFoundException si no se encuentra el negocio
     */
    @Transactional
    public NegocioResponseDTO update(Long id, NegocioRequestDTO dto) {
        Negocio negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Negocio no encontrado con id: " + id
                ));

        if (!negocio.getSlug().equals(dto.getSlug()) &&
                negocioRepository.findBySlug(dto.getSlug()).isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe un negocio con el slug: " + dto.getSlug()
            );
        }

        negocioMapper.updateEntityFromDTO(dto, negocio);

        // Hasheamos la contraseña si viene
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            negocio.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Negocio updatedNegocio = negocioRepository.save(negocio);

        return negocioMapper.toResponseDTO(updatedNegocio);
    }

    /**
     * Desactiva un negocio (borrado lógico).
     *
     * @param id ID del negocio a desactivar
     * @throws EntityNotFoundException si no se encuentra el negocio
     */
    @Transactional
    public void delete(Long id) {
        Negocio negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Negocio no encontrado con id: " + id
                ));

        negocio.setActivo(false);
        negocioRepository.save(negocio);
    }
}
