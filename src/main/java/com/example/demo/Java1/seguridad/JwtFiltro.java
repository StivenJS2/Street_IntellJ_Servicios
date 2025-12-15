package com.example.demo.Java1.seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFiltro extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("Token recibido");

            if (jwtUtil.validarToken(token)) {
                String usuario = jwtUtil.obtenerUsuario(token);
                String rol = jwtUtil.obtenerRol(token);

                System.out.println("Usuario JWT: " + usuario);
                System.out.println("Rol JWT: " + rol);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                usuario,
                                null,
                                List.of(new SimpleGrantedAuthority(rol))
                        );

                System.out.println("Authorities: " + authentication.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("Token inválido");
            }
        } else {
            System.out.println("No viene Authorization Bearer");
        }

        filterChain.doFilter(request, response);
    }
}
