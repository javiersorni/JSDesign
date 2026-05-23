package com.example.evento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.evento.entity.Grupo;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    List<Grupo> findByEventoId(Long eventoId);
}
