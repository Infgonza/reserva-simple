package com.rs.reserva_simple.service;


import com.rs.reserva_simple.mapper.NegocioMapper;
import com.rs.reserva_simple.persistance.dto.request.NegocioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.NegocioResponseDTO;
import com.rs.reserva_simple.persistance.entity.Negocio;
import com.rs.reserva_simple.persistance.entity.enums.RolPlataforma;
import com.rs.reserva_simple.persistance.repository.NegocioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

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
     * Busca un negocio por slug
     */
    @Transactional(readOnly = true)
    public NegocioResponseDTO findBySlug(String slug) {
        Negocio negocio = negocioRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Negocio no encontrado con slug: " + slug
                ));
        return negocioMapper.toResponseDTO(negocio);
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

        // 1. GENERAR SLUG a partir del nombre del negocio
        String nombreNegocio = dto.getNombre();
        String slugGenerado = toSlug(nombreNegocio);

        // 2. VERIFICAR UNICIDAD DE EMAIL
        if (negocioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe un negocio registrado con el email: " + dto.getEmail()
            );
        }

        // 3. CORREGIR LA VERIFICACIÓN DE SLUG para usar el valor generado
        if (negocioRepository.findBySlug(slugGenerado).isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe un negocio con el slug: " + slugGenerado
            );
        }

        Negocio negocio = negocioMapper.toEntity(dto);

        // 4. ASIGNAR EL SLUG GENERADO ANTES DE GUARDAR
        negocio.setSlug(slugGenerado);

        negocio.setPassword(passwordEncoder.encode(dto.getPassword()));
        negocio.setRolPlataforma(RolPlataforma.USUARIO); // Asumo que se crea con rol USUARIO
        negocio.setActivo(true);

        Negocio savedNegocio = negocioRepository.save(negocio);

        return negocioMapper.toResponseDTO(savedNegocio);
    }


    /**
     * Convierte un texto en un slug amigable para URLs.
     * Si el texto es nulo o vacío, genera un slug por defecto con timestamp.
     *
     * @param text Texto a convertir
     * @return Slug generado
     */
    public static String toSlug(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "negocio-" + System.currentTimeMillis();
        }
        return text.toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9\\-]+", "")
                .replaceAll("^-|-$", "")
                .trim();
    }


    /**
     * Actualiza el perfil del negocio usando el ID del Negocio de la sesión.
     * @param id El ID del Negocio logueado.
     * @param requestDTO Los nuevos datos del perfil.
     * @return NegocioResponseDTO actualizado.
     */
    public NegocioResponseDTO updateNegocioProfile(Long id, NegocioRequestDTO requestDTO) {

        // 1. Buscar y verificar que el Negocio existe
        Negocio negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Negocio no encontrado con id: " + id
                ));

        negocio.setSlug(requestDTO.getSlug());
        negocio.setDireccion(requestDTO.getDireccion());
        negocio.setDescripcion(requestDTO.getDescripcion());
        negocio.setEmail(requestDTO.getEmail());

        if (requestDTO.getProfileImageUrl() != null) {
            negocio.setProfileImageUrl(requestDTO.getProfileImageUrl());
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
