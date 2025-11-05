package com.rs.reserva_simple.persistance.repository;

import com.rs.reserva_simple.persistance.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
}
