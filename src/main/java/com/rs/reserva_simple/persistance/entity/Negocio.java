package com.rs.reserva_simple.persistance.entity;

import jakarta.persistence.*;
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
public class Negocio extends BaseEntity{

    private String nombre;
    private String slug;
    private String direccion;
    private String telefono;
    private String email;
    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario propietario;

    @OneToMany(
            mappedBy = "negocio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Servicio> servicios = new ArrayList<>();

    @OneToMany(mappedBy = "negocio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<UsuarioNegocio> empleados = new ArrayList<>();

    @OneToMany(mappedBy = "negocio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Turno> turnos = new ArrayList<>();

    @OneToMany(mappedBy = "negocio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Cliente> clientes = new ArrayList<>();

}
