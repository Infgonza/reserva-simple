package com.rs.reserva_simple.persistance.repository;

import com.rs.reserva_simple.persistance.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
