package com.phonecorp.phonecorpbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion de seguridad — estructura lista para roles futuros.
 *
 * Estado actual: permite todo el trafico (fase de desarrollo).
 * Para activar seguridad por roles, descomentar los bloques indicados
 * y agregar el filtro JWT correspondiente.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF: arquitectura stateless con JWT no requiere tokens CSRF
            .csrf(csrf -> csrf.disable())

            // Politica de sesion stateless (JWT no usa sesiones HTTP)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Control de acceso — FASE DE DESARROLLO: permitir todo
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
                // --- DESCOMENTAR para activar seguridad por roles ---
                // .requestMatchers("/api/clientes/**").hasAnyRole("ADMIN", "VENTAS")
                // .requestMatchers("/api/ordenes/**").hasAnyRole("ADMIN", "VENTAS")
                // .requestMatchers("/api/pagos/**").hasRole("FACTURACION")
                // .requestMatchers("/api/inventario/**").hasRole("INVENTARIO")
                // .requestMatchers("/api/postventa/**").hasAnyRole("ADMIN", "POSTVENTA")
                // .anyRequest().authenticated()
                // ----------------------------------------------------
            );

            // --- DESCOMENTAR para agregar filtro JWT ---
            // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}