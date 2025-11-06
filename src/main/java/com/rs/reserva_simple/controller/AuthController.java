package com.rs.reserva_simple.controller;

import com.rs.reserva_simple.persistance.dto.request.UsuarioRequestDTO;
import com.rs.reserva_simple.persistance.dto.response.UsuarioResponseDTO;
import com.rs.reserva_simple.security.dto.LoginRequestDTO;
import com.rs.reserva_simple.security.dto.LoginResponseDTO;
import com.rs.reserva_simple.security.jwt.JwtUtilsService;
import com.rs.reserva_simple.service.CustomUserDetailsService;
import com.rs.reserva_simple.service.UsuarioService;
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

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtilsService jwtUtilsService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request){
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
        final String token = jwtUtilsService.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> register(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO usuario = usuarioService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }
}
