package com.rs.reserva_simple.persistance.repository;

import com.rs.reserva_simple.persistance.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    @Query("SELECT s FROM Servicio s JOIN FETCH s.negocio WHERE s.id = :id")
    Optional<Servicio> findByIdWithNegocio(@Param("id") Long id);

    List<Servicio> findByNegocioId(Long negocioId);
}
