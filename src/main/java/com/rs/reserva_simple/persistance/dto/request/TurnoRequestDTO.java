package com.rs.reserva_simple.persistance.dto.request;

import com.rs.reserva_simple.persistance.entity.enums.Estado;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnoRequestDTO {

    @NotNull(message = "La fecha del turno es obligatoria")
    private LocalDate fechaTurno;

    @NotNull(message = "La hora de inicio es obligatoria")
    private Instant horaInicio;

    @NotNull(message = "La hora final es obligatoria")
    private Instant horaFinal;

    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    @Size(max = 1000, message = "Las notas no pueden exceder 1000 caracteres")
    private String notas;

    @NotNull(message = "El ID del negocio es obligatorio")
    private Long negocioId;

    @NotNull(message = "El ID del servicio es obligatorio")
    private Long servicioId;

    @NotNull(message = "El ID del empleado (usuario-negocio) es obligatorio")
    private Long usuarioNegocioId;

    private Long clienteId; // Opcional: puede ser null para turnos sin cliente asignado
}
