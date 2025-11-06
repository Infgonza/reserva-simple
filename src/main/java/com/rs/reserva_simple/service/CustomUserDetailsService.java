package com.rs.reserva_simple.service;

import com.rs.reserva_simple.persistance.entity.Negocio;
import com.rs.reserva_simple.persistance.repository.NegocioRepository;
import com.rs.reserva_simple.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio para cargar detalles del negocio autenticado.
 * Usado por Spring Security para autenticación.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final NegocioRepository negocioRepository;

    /**
     * Carga un negocio por su email (username).
     *
     * @param email Email del negocio (usado como username)
     * @return UserDetails con la información del negocio
     * @throws UsernameNotFoundException si no se encuentra el negocio
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Negocio negocio = negocioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Negocio no encontrado con email: " + email
                ));

        return new CustomUserDetails(negocio);
    }
}
