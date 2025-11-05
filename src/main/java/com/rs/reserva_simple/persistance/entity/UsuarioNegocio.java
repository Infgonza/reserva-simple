package com.rs.reserva_simple.persistance.entity;

import com.rs.reserva_simple.persistance.entity.enums.RolNegocio;
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
public class UsuarioNegocio extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    @Enumerated(EnumType.STRING)
    private RolNegocio rolNegocio;

    private Boolean activo;

    @OneToMany(mappedBy = "usuarioNegocio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Turno> turnos = new ArrayList<>();
}
