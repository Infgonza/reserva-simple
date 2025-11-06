package com.rs.reserva_simple.security;

import com.rs.reserva_simple.persistance.entity.Negocio;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


public class CustomUserDetails implements UserDetails {

    private final Negocio negocio;

    public CustomUserDetails(Negocio negocio) {
        this.negocio = negocio;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // El rol siempre es USUARIO para Negocio
        return List.of(new SimpleGrantedAuthority(negocio.getRolPlataforma().name()));
    }

    @Override
    public String getPassword() {
        return negocio.getPassword();
    }

    @Override
    public String getUsername() {
        // Usa el email como username
        return negocio.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // El negocio está habilitado si está activo
        return negocio.getActivo();
    }

    /**
     * Obtiene la entidad Negocio completa
     */
    public Negocio getNegocio() {
        return negocio;
    }

    /**
     * Obtiene el ID del negocio autenticado
     */
    public Long getNegocioId() {
        return negocio.getId();
    }
}
