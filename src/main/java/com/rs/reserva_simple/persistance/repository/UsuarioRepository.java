package com.rs.reserva_simple.persistance.repository;

import com.rs.reserva_simple.persistance.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByEmailAndPassword(String email, String password);

}
