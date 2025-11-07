package com.rs.reserva_simple.service;

import com.rs.reserva_simple.mapper.TurnoMapper;
import com.rs.reserva_simple.persistance.dto.response.TurnoResponseDTO;
import com.rs.reserva_simple.persistance.entity.Turno;
import com.rs.reserva_simple.persistance.repository.NegocioRepository;
import com.rs.reserva_simple.persistance.repository.ServicioRepository;
import com.rs.reserva_simple.persistance.repository.TurnoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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



}
