package com.rs.reserva_simple.persistance.entity;

import com.rs.reserva_simple.persistance.entity.enums.RolPlataforma;
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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_plataforma", nullable = false)
    private RolPlataforma rolPlataforma = RolPlataforma.USUARIO;

    // CAMPOS DE INFORMACIÓN DEL NEGOCIO

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String slug;

    private String direccion;
    private String telefono;
    private String descripcion; // Campo extra integrado desde Usuario
    private String profileImageUrl; // Campo extra integrado desde Usuario

    @Column(nullable = false)
    private Boolean activo = true;

    // RELACIONES

    @OneToMany(
            mappedBy = "negocio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Servicio> servicios = new ArrayList<>();

    @OneToMany(
            mappedBy = "negocio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<UsuarioNegocio> empleados = new ArrayList<>();

    @OneToMany(
            mappedBy = "negocio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Turno> turnos = new ArrayList<>();

    @OneToMany(
            mappedBy = "negocio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Cliente> clientes = new ArrayList<>();
}
