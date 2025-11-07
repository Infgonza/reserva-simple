package com.rs.reserva_simple.service;

import com.rs.reserva_simple.mapper.TurnoMapper;
import com.rs.reserva_simple.persistance.repository.NegocioRepository;
import com.rs.reserva_simple.persistance.repository.ServicioRepository;
import com.rs.reserva_simple.persistance.repository.TurnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final TurnoMapper turnoMapper;
    private final NegocioRepository negocioRepository; // Por las dudas
    private final ServicioRepository servicioRepository; // Por las dudas
    //private final ClienteRepository clienteRepository; // 

}
