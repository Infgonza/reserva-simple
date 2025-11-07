package com.rs.reserva_simple.persistance.entity;

import com.rs.reserva_simple.persistance.entity.enums.Estado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Turno extends BaseEntity{

    @Column(name = "fecha_turno")
    private LocalDate fechaTurno;

    @Column(name = "hora_inicio")
    private OffsetDateTime horaInicio;

    @Column(name = "hora_final")
    private OffsetDateTime horaFinal;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private String notas;

    @ManyToOne
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "usuario_negocio_id")
    private UsuarioNegocio usuarioNegocio;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
