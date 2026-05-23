package com.example.evento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.example.evento.entity.Mesa;

import jakarta.transaction.Transactional;

public interface MesaRepository extends JpaRepository<Mesa, Long> {

    List<Mesa> findByEventoId(Long eventoId);

    @Transactional
    @Modifying
    void deleteByEventoId(Long eventoId);

    List<Mesa> findByEventoIdOrderByNumeroMesaAsc(Long eventoId);
}
