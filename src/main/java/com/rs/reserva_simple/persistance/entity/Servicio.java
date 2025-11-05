package com.rs.reserva_simple.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Servicio extends BaseEntity{

    private String nombre;
    private String descripcion;
    private Integer duracion;
    private Double precio;
    private String color;
    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    @OneToMany(mappedBy = "servicio")
    private List<Turno> turnos = new ArrayList<>();
}
