package com.rs.reserva_simple.controller;

import com.rs.reserva_simple.service.TurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;

}
