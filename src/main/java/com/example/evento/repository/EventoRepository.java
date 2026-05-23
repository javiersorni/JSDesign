package com.example.evento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.evento.entity.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}
