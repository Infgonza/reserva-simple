package com.rs.reserva_simple.controller;

import com.rs.reserva_simple.persistance.dto.request.NegocioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.NegocioResponseDTO;
import com.rs.reserva_simple.security.dto.LoginRequestDTO;
import com.rs.reserva_simple.security.dto.LoginResponseDTO;
import com.rs.reserva_simple.security.jwt.JwtUtilsService;
import com.rs.reserva_simple.service.CustomUserDetailsService;
import com.rs.reserva_simple.service.NegocioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de autenticación para Negocios.
 * Maneja registro y login de propietarios de negocios.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final NegocioService negocioService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtilsService jwtUtilsService;
    private final AuthenticationManager authenticationManager;

    /**
     * Endpoint de login para propietarios de negocios.
     *
     * @param request Credenciales de login (email y contraseña)
     * @return Token JWT si la autenticación es exitosa
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());

        final String token = jwtUtilsService.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    /**
     * Endpoint de registro para nuevos negocios.
     * Crea un negocio con rol PROPIETARIO automáticamente.
     *
     * @param dto Datos del negocio a registrar
     * @return Datos del negocio registrado (sin contraseña)
     */
    @PostMapping("/register")
    public ResponseEntity<NegocioResponseDTO> register(@Valid @RequestBody NegocioRequestDTO dto) {
        NegocioResponseDTO negocio = negocioService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(negocio);
    }
}
