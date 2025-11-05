package com.rs.reserva_simple.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SerivicioRepository extends JpaRepository<SerivicioRepository, Long> {
}
