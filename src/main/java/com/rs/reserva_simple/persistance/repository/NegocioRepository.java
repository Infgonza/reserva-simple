package com.rs.reserva_simple.persistance.repository;

import com.rs.reserva_simple.persistance.entity.Negocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NegocioRepository extends JpaRepository<Negocio, Long> {
    Optional<Negocio> findByEmail(String email);

    Optional<Negocio> findBySlug(String slug);


}
