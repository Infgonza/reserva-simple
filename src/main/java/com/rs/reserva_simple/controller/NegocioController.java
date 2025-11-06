package com.rs.reserva_simple.controller;

import com.rs.reserva_simple.persistance.dto.request.NegocioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.NegocioResponseDTO;
import com.rs.reserva_simple.service.NegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/negocios")
public class NegocioController {

    private final NegocioService negocioService;

    @GetMapping("/{id}")
    public ResponseEntity<NegocioResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(negocioService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<NegocioResponseDTO>> findAll() {
        return ResponseEntity.ok(negocioService.findAll());
    }

    public ResponseEntity<NegocioResponseDTO> create(@RequestHeader Long idUsuario, @RequestBody NegocioRequestDTO dto){
        return ResponseEntity.ok(negocioService.create(idUsuario, dto));
    }

}
