package com.rs.reserva_simple.service;

import com.rs.reserva_simple.mapper.ServicioMapper;
import com.rs.reserva_simple.persistance.dto.request.ServicioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.ServicioResponseDTO;
import com.rs.reserva_simple.persistance.entity.Negocio;
import com.rs.reserva_simple.persistance.entity.Servicio;
import com.rs.reserva_simple.persistance.repository.NegocioRepository;
import com.rs.reserva_simple.persistance.repository.ServicioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;
    private final NegocioRepository negocioRepository;
    private final ServicioMapper servicioMapper;

    /**
     * Obtiene todos los servicios de un negocio
     * @param negocioId ID para buscar los servicios pertenecientes a ese negocio
     * @return Lista de servicioDTO
     */
    @Transactional(readOnly = true)
    public List<ServicioResponseDTO> findAllByNegocio(Long negocioId){
        negocioRepository.findById(negocioId)
                .orElseThrow(()-> new EntityNotFoundException("Negocio no encontrado para id: " + negocioId));

        List<Servicio> servicios = servicioRepository.findByNegocioId(negocioId);

        return servicios.stream().map(servicioMapper::toResponseDTO).toList();
    }

    /**
     * Obtiene un servicio por ID y verifica que pertenezca al negocio
     * @param id ID del servicio a obtener
     * @param negocioId ID del negocio al que pertenece
     * @return ServicioResponseDTO del servicio encontrado
     */
    @Transactional(readOnly = true)
    public ServicioResponseDTO findById(Long id, Long negocioId){
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Servicio no encontrado con id: " + id));

        if (!servicio.getNegocio().getId().equals(negocioId)){
            throw new IllegalArgumentException("Este servicio no pertenece a su negocio");
        }

        return servicioMapper.toResponseDTO(servicio);
    }

    public ServicioResponseDTO create(Long negocioId, ServicioRequestDTO dto){

        Negocio negocio = negocioRepository.findById(negocioId)
                .orElseThrow(() -> new EntityNotFoundException("Negocio no encontrado con id: " + negocioId));

        if (!negocio.getActivo()) {
            throw new IllegalArgumentException("El negocio no está activo");
        }
        Servicio servicio = servicioMapper.toEntity(dto);
        servicio.setNegocio(negocio);
        Servicio savedServicio = servicioRepository.save(servicio);

        return servicioMapper.toResponseDTO(savedServicio);
    }

    /**
     * Actualiza un servicio existente
     * @param id ID del servicio a actualizar
     * @param negocioId ID del negocio al que pertenece
     * @param dto Datos actualizados del servicio
     * @return ServicioResponseDTO del servicio actualizado
     */
    @Transactional
    public ServicioResponseDTO update(Long id, Long negocioId, ServicioRequestDTO dto) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Servicio no encontrado con id: " + id
                ));

        if (!servicio.getNegocio().getId().equals(negocioId)) {
            throw new IllegalArgumentException(
                    "Este servicio no pertenece a su negocio"
            );
        }
        servicioMapper.updateEntityFromDTO(dto, servicio);
        Servicio updatedServicio = servicioRepository.save(servicio);

        return servicioMapper.toResponseDTO(updatedServicio);
    }


    /**
     * Elimina (desactiva) un servicio existente
     * @param id ID del servicio a eliminar
     * @param negocioId ID del negocio al que pertenece
     */
    @Transactional
    public void delete(Long id, Long negocioId) {

        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Servicio no encontrado con id: " + id
                ));
        if (!servicio.getNegocio().getId().equals(negocioId)) {
            throw new IllegalArgumentException(
                    "Este servicio no pertenece a su negocio"
            );
        }
        servicio.setActivo(false);
        servicioRepository.delete(servicio);
    }
}
