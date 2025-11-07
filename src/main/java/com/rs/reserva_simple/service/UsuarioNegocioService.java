package com.rs.reserva_simple.service;

import com.rs.reserva_simple.mapper.UsuarioNegocioMapper;
import com.rs.reserva_simple.persistance.dto.request.UsuarioNegocioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.UsuarioNegocioResponseDTO;
import com.rs.reserva_simple.persistance.entity.Negocio;
import com.rs.reserva_simple.persistance.entity.UsuarioNegocio;
import com.rs.reserva_simple.persistance.entity.enums.RolNegocio;
import com.rs.reserva_simple.persistance.repository.NegocioRepository;
import com.rs.reserva_simple.persistance.repository.UsuarioNegocioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioNegocioService {

    private final UsuarioNegocioRepository empleadoRepository;
    private final NegocioRepository negocioRepository;
    private final UsuarioNegocioMapper empleadoMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Obtiene todos los empleados de un negocio específico.
     *
     * @param negocioId ID del negocio propietario
     * @return Lista de empleados del negocio
     */
    @Transactional(readOnly = true)
    public List<UsuarioNegocioResponseDTO> findAllByNegocio(Long negocioId) {
        List<UsuarioNegocio> empleados = empleadoRepository.findByNegocioId(negocioId);
        return empleados.stream()
                .map(empleadoMapper::toResponseDTO)
                .toList();
    }

    /**
     * Obtiene un empleado específico verificando que pertenezca al negocio.
     *
     * @param id ID del empleado
     * @param negocioId ID del negocio propietario
     * @return Datos del empleado
     * @throws EntityNotFoundException si no se encuentra o no pertenece al negocio
     */
    @Transactional(readOnly = true)
    public UsuarioNegocioResponseDTO findById(Long id, Long negocioId) {
        UsuarioNegocio empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Empleado no encontrado con id: " + id
                ));

        if (!empleado.getNegocio().getId().equals(negocioId)) {
            throw new IllegalArgumentException(
                    "Este empleado no pertenece a su negocio"
            );
        }

        return empleadoMapper.toResponseDTO(empleado);
    }

    /**
     * Crea un nuevo empleado para el negocio autenticado.
     * Asigna automáticamente el rol EMPLEADO.
     *
     * @param negocioId ID del negocio propietario
     * @param dto Datos del empleado a crear
     * @return Datos del empleado creado
     * @throws EntityNotFoundException si no se encuentra el negocio
     * @throws IllegalArgumentException si el email ya está en uso
     */
    @Transactional
    public UsuarioNegocioResponseDTO create(Long negocioId, UsuarioNegocioRequestDTO dto) {

        Negocio negocio = negocioRepository.findById(negocioId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Negocio no encontrado con id: " + negocioId
                ));

        if (empleadoRepository.existsByEmailAndNegocioId(dto.getEmail(), negocioId)) {
            throw new IllegalArgumentException(
                    "Ya existe un empleado con el email: " + dto.getEmail()
            );
        }

        UsuarioNegocio empleado = empleadoMapper.toEntity(dto);

        empleado.setNegocio(negocio);

        empleado.setRolNegocio(RolNegocio.EMPLEADO);

        String password = dto.getPassword();
        if (password == null || password.isBlank()) {
            password = generateRandomPassword();
        }
        empleado.setPassword(passwordEncoder.encode(password));

        empleado.setActivo(true);

        UsuarioNegocio savedEmpleado = empleadoRepository.save(empleado);

        return empleadoMapper.toResponseDTO(savedEmpleado);
    }

    /**
     * Actualiza un empleado existente.
     * Verifica que pertenezca al negocio autenticado.
     *
     * @param id ID del empleado a actualizar
     * @param negocioId ID del negocio propietario
     * @param dto Datos actualizados
     * @return Datos del empleado actualizado
     * @throws EntityNotFoundException si no se encuentra el empleado
     * @throws IllegalArgumentException si no pertenece al negocio
     */
    @Transactional
    public UsuarioNegocioResponseDTO update(Long id, Long negocioId, UsuarioNegocioRequestDTO dto) {

        UsuarioNegocio empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Empleado no encontrado con id: " + id
                ));

        if (!empleado.getNegocio().getId().equals(negocioId)) {
            throw new IllegalArgumentException(
                    "Este empleado no pertenece a su negocio"
            );
        }

        if (!empleado.getEmail().equals(dto.getEmail()) &&
                empleadoRepository.existsByEmailAndNegocioId(dto.getEmail(), negocioId)) {
            throw new IllegalArgumentException(
                    "Ya existe un empleado con el email: " + dto.getEmail()
            );
        }

        empleadoMapper.updateEntityFromDTO(dto, empleado);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            empleado.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        UsuarioNegocio updatedEmpleado = empleadoRepository.save(empleado);

        return empleadoMapper.toResponseDTO(updatedEmpleado);
    }

    /**
     * Desactiva un empleado (borrado lógico).
     * Verifica que pertenezca al negocio autenticado.
     *
     * @param id ID del empleado a desactivar
     * @param negocioId ID del negocio propietario
     * @throws EntityNotFoundException si no se encuentra el empleado
     * @throws IllegalArgumentException si no pertenece al negocio
     */
    @Transactional
    public void delete(Long id, Long negocioId) {
        UsuarioNegocio empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Empleado no encontrado con id: " + id
                ));

        if (!empleado.getNegocio().getId().equals(negocioId)) {
            throw new IllegalArgumentException(
                    "Este empleado no pertenece a su negocio"
            );
        }

        empleado.setActivo(false);
        empleadoRepository.delete(empleado);
    }

    /**
     * Genera una contraseña aleatoria para empleados sin contraseña inicial.
     */
    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 12);
    }
}
