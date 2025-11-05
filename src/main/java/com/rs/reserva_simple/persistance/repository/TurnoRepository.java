package com.rs.reserva_simple.persistance.repository;

import com.rs.reserva_simple.persistance.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {
}
