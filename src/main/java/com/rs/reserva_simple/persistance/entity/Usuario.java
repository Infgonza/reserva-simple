package com.rs.reserva_simple.persistance.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Usuario extends BaseEntity {

    private String email;
    private String contraseña;
    private String nombre;
    private String telefono;
    @Enumerated(EnumType.STRING)
    @Column(name = "rol_plataforma")
    private RolPlataforma rolPlataforma;

    @OneToMany(
            mappedBy = "propietario",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Negocio> negocios = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<UsuarioNegocio> trabaja;


}
