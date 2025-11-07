package com.rs.reserva_simple.service;

import com.rs.reserva_simple.mapper.ClienteMapper;
import com.rs.reserva_simple.mapper.TurnoMapper;
import com.rs.reserva_simple.persistance.dto.response.TurnoResponseDTO;
import com.rs.reserva_simple.persistance.entity.Turno;
import com.rs.reserva_simple.persistance.repository.NegocioRepository;
import com.rs.reserva_simple.persistance.repository.ServicioRepository;
import com.rs.reserva_simple.persistance.repository.TurnoRepository;
import jakarta.persistence.EntityNotFoundException;

import com.rs.reserva_simple.mapper.UsuarioNegocioMapper;
import com.rs.reserva_simple.persistance.dto.request.TurnoRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.TurnoResponseDTO;
import com.rs.reserva_simple.persistance.dto.response.basic.EmpleadoBasicDTO;
import com.rs.reserva_simple.persistance.entity.*;
import com.rs.reserva_simple.persistance.entity.enums.Estado;
import com.rs.reserva_simple.persistance.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional; 
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final TurnoMapper turnoMapper;
    private final NegocioRepository negocioRepository; // Por las dudas
    private final ServicioRepository servicioRepository; // Por las dudas
    //private final ClienteRepository clienteRepository; //


    /**
     * Obtiene un turno por ID y verifica que pertenece al negocio que lo busco
     * @param id ID del turno a buscar
     * @param negocioId ID del negocio al que pertenece el turno
     * @return TurnoResponseDTO
     */
    @Transactional(readOnly = true)
    public TurnoResponseDTO findById(Long id, Long negocioId){
        negocioRepository.findById(negocioId)
                .orElseThrow(()-> new EntityNotFoundException("Negocio no encontrado con id: " + id));

        Turno turno = turnoRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Turno no encontrado con id: " + id));

        return turnoMapper.toResponseDTO(turno);
    }

    @Transactional(readOnly = true)
    public List<TurnoResponseDTO> findAll(Long negocioId){

        List<Turno> turnos= turnoRepository.findByNegocioId(negocioId);

        return turnos.stream().map(turnoMapper::toResponseDTO).toList();
    }

    public void delete(Long id, Long negocioId){
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("No existe un turno con el id: " + id));

        if (!turno.getNegocio().getId().equals(negocioId)){
            throw new IllegalArgumentException("El Turno no pertenece a ese Negocio");
        }

        turnoRepository.delete(turno);
    }



    private final NegocioRepository negocioRepository;
    private final ServicioRepository servicioRepository;
    private final ClienteMapper clienteMapper;
    private final ClienteRepository clienteRepository;
    private final UsuarioNegocioRepository usuarioNegocioRepository;
    private final UsuarioNegocioMapper usuarioNegocioMapper;

    /**
     * Obtiene todos los empleados activos del negocio asociado al servicio.
     * @param servicioId ID del servicio seleccionado.
     * @return Lista de EmpleadoBasicDTO.
     * @throws EntityNotFoundException si el servicio no existe.
     */
    @Transactional
    public List<EmpleadoBasicDTO> getActiveEmployeesByService(Long servicioId) {
        // 1. Obtener el servicio para encontrar su negocioId
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado con ID: " + servicioId));

        Long negocioId = servicio.getNegocio().getId();

        // 2. Obtener todos los empleados activos de ese negocio
        List<UsuarioNegocio> empleados = usuarioNegocioRepository.findByNegocioIdAndActivoTrue(negocioId);

        return empleados.stream()
                .map(usuarioNegocioMapper::toEmpleadoBasicDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los turnos para una fecha y empleado específicos
     * @param fecha Fecha a consultar
     * @param empleadoId ID del empleado
     * @return Lista de turnos
     */
    @Transactional
    public List<TurnoResponseDTO> getTurnosByFechaYEmpleado(LocalDate fecha, Long empleadoId) {
        List<Turno> turnos = turnoRepository.findByFechaTurnoAndUsuarioNegocioId(fecha, empleadoId);

        return turnos.stream()
                .map(turnoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TurnoResponseDTO create(String negocioSlug, TurnoRequestDTO requestDTO) {
        Cliente cliente;

        // Buscar por Email O Teléfono para reutilizar el cliente existente
        Optional<Cliente> existingCliente = clienteRepository.findByEmailOrTelefono(
                requestDTO.getCliente().getEmail(),
                requestDTO.getCliente().getTelefono()
        );

        if (existingCliente.isPresent()) {
            cliente = existingCliente.get();
        } else {
            // Cliente nuevo
            cliente = clienteMapper.toEntity(requestDTO.getCliente());
        }

        // Obtener Negocio
        Negocio negocio = negocioRepository.findBySlug(negocioSlug)
                .orElseThrow(() -> new EntityNotFoundException("Negocio no encontrado con slug: " + negocioSlug));

        // Obtener Servicio
        Servicio servicio = servicioRepository.findById(requestDTO.getServicioId())
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado con ID: " + requestDTO.getServicioId()));

        // Obtener empleado
        UsuarioNegocio empleado = null;
        if (requestDTO.getUsuarioNegocioId() != null) {
            empleado = usuarioNegocioRepository.findById(requestDTO.getUsuarioNegocioId())
                    .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + requestDTO.getUsuarioNegocioId()));

            if (!empleado.getNegocio().getId().equals(negocio.getId())) {
                throw new IllegalArgumentException("El empleado seleccionado no pertenece a este negocio.");
            }
        }

        if (cliente.getId() == null) {
            cliente.setNegocio(negocio);
            cliente = clienteRepository.save(cliente);
        }

        // Crear y guardar turno
        Turno turno = turnoMapper.toEntity(requestDTO);
        turno.setCliente(cliente);
        turno.setNegocio(negocio);
        turno.setServicio(servicio);
        turno.setUsuarioNegocio(empleado);
        turno.setEstado(Estado.PENDIENTE);

        Turno savedTurno = turnoRepository.save(turno);


        return turnoMapper.toResponseDTO(savedTurno);
    }
}