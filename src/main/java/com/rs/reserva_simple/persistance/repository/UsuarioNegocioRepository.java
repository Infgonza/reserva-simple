package com.rs.reserva_simple.persistance.repository;

import com.rs.reserva_simple.persistance.entity.UsuarioNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioNegocioRepository extends JpaRepository<UsuarioNegocio, Long> {
    List<UsuarioNegocio> findByNegocioId(Long id);

    boolean existsByEmailAndNegocioId(String email, Long id);


}
