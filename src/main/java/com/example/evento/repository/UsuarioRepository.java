package com.example.evento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.evento.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
}