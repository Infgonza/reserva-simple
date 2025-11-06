package com.rs.reserva_simple.service;

import com.rs.reserva_simple.mapper.UsuarioMapper;
import com.rs.reserva_simple.persistance.dto.request.UsuarioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.UsuarioResponseDTO;
import com.rs.reserva_simple.persistance.entity.Usuario;
import com.rs.reserva_simple.persistance.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> findAll(){

        List<Usuario> usuarios = usuarioRepository.findAll();

        return usuarios.stream().map(usuarioMapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO findById(Long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return usuarioMapper.toResponseDTO(usuario);
    }

    public UsuarioResponseDTO create(UsuarioRequestDTO dto){
        Usuario usuario = usuarioRepository.save(usuarioMapper.toEntity(dto));
        return usuarioMapper.toResponseDTO(usuario);
    }

    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto){

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuario no encontrado con ID: " + id
                ));
        usuarioMapper.updateEntityFromDTO(dto, usuario);

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    // TODO funcion para login (email + contraseña)

    // TODO Cambiar para borrado lógico
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Usuario no encontrado con ID: " + id
            );
        }

        usuarioRepository.deleteById(id);
    }
}
