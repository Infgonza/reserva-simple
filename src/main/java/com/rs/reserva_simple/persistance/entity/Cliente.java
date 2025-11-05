package com.rs.reserva_simple.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cliente extends BaseEntity{

    private String nombre;
    private String telefono;
    private String email;
    private String notas;

    @Column(name = "cantidad_turnos")
    private Integer cantidadTurnos;

    @Column(name = "fecha_primer_turno")
    private Instant fechaPrimerTurno;

    @Column(name = "ultimo_turno")
    private Instant fechaUltimoTurno;

    @ManyToOne
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    @OneToMany(
            mappedBy = "cliente",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Turno> turnos = new ArrayList<>();

}
