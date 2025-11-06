package com.rs.reserva_simple.service;


import com.rs.reserva_simple.mapper.NegocioMapper;
import com.rs.reserva_simple.persistance.dto.request.NegocioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.NegocioResponseDTO;
import com.rs.reserva_simple.persistance.entity.Negocio;
import com.rs.reserva_simple.persistance.entity.Usuario;
import com.rs.reserva_simple.persistance.repository.NegocioRepository;
import com.rs.reserva_simple.persistance.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NegocioService {

    private final NegocioRepository negocioRepository;
    private final NegocioMapper negocioMapper;
    private final UsuarioRepository usuarioRepository;

    /**
     * Busca todos los negocios
     * @return Lista de NegocioResponseDTO
     */
    @Transactional(readOnly = true)
    public List<NegocioResponseDTO> findAll (){

        List<Negocio> negocios = negocioRepository.findAll();

        return negocios.stream().map(negocioMapper::toResponseDTO).toList();
    }

    /**
     * Busca un negocio por ID
     * @param id ID del negocio
     * @throws EntityNotFoundException si no se encuentra el negocio
     * @return NegocioResponseDTO
     */
    @Transactional(readOnly = true)
    public NegocioResponseDTO findById(Long id){
        Negocio negocio = negocioRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Negocio no encontrado con id: " + id));
        return negocioMapper.toResponseDTO(negocio);
    }

    /**
     *
     * @param idUsuario ID del usuario propietario del negocio
     * @param dto Datos del negocio a crear
     * @throws EntityNotFoundException si no se encuentra el usuario propietario
     * @return NegocioResponseDTO del negocio creado
     */
    @Transactional
    public NegocioResponseDTO create(Long idUsuario, NegocioRequestDTO dto){

        Usuario propietario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + idUsuario));
        Negocio negocio = negocioMapper.toEntity(dto);
        negocio.setPropietario(propietario);

        Negocio savedNegocio = negocioRepository.save(negocio);

        return negocioMapper.toResponseDTO(savedNegocio);
    }

    /**
     *
     * @param idUsuario ID del usuario propietario del negocio
     * @param idNegocio ID del negocio a actualizar
     * @param dto Datos del negocio a actualizar
     * @throws EntityNotFoundException si no se encuentra el usuario propietario o el negocio
     * @throws IllegalArgumentException si el usuario no es el propietario del negocio
     * @return NegocioResponseDTO del negocio actualizado
     */
    @Transactional
    public NegocioResponseDTO update(Long idUsuario, Long idNegocio, NegocioRequestDTO dto){
        Usuario propietario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + idUsuario));
        Negocio negocio = negocioRepository.findById(idNegocio)
                .orElseThrow(()-> new EntityNotFoundException("Negocio no encontrado con id: " + idNegocio));
        if(!negocio.getPropietario().getId().equals(propietario.getId())) {
            throw new IllegalArgumentException("El usuario no es el propietario del negocio");
        }
        negocioMapper.updateEntityFromDTO(dto, negocio);

        Negocio updatedNegocio = negocioRepository.save(negocio);
        return negocioMapper.toResponseDTO(updatedNegocio);
    }

    //TODO funcion para borrar un negocio (borrado lógico)
}
