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
@Table(name = "usuario_negocio")
public class UsuarioNegocio extends BaseEntity{

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String email;

    private String telefono;

    private String password;

    // ROL Y ESTADO

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolNegocio rolNegocio = RolNegocio.EMPLEADO; // Siempre EMPLEADO

    @Column(nullable = false)
    private Boolean activo = true;

    // RELACIONES

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "negocio_id", nullable = false)
    private Negocio negocio;

    @OneToMany(
            mappedBy = "usuarioNegocio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Turno> turnos = new ArrayList<>();

    // MÉTODOS DE UTILIDAD

    public boolean isActivo() {
        return this.activo;
    }

    public String getNombreNegocio() {
        return this.negocio != null ? this.negocio.getNombre() : null;
    }
}
