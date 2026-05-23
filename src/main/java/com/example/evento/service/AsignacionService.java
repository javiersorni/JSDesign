package com.example.evento.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.evento.entity.AsignacionMesa;
import com.example.evento.entity.Grupo;
import com.example.evento.entity.Mesa;
import com.example.evento.repository.AsignacionMesaRepository;
import com.example.evento.repository.GrupoRepository;
import com.example.evento.repository.MesaRepository;

@Service
public class AsignacionService {

    private final AsignacionMesaRepository repo;
    private final MesaRepository mesaRepository;
    private final GrupoRepository grupoRepository;

    public AsignacionService(
            AsignacionMesaRepository repo,
            MesaRepository mesaRepository,
            GrupoRepository grupoRepository) {
        this.repo = repo;
        this.mesaRepository = mesaRepository;
        this.grupoRepository = grupoRepository;
    }

    @Transactional
    public void asignarGrupo(Long grupoId, List<Mesa> mesas) {

        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        if (yaAsignado(grupoId)) {
            throw new IllegalStateException("Este grupo ya tiene mesas asignadas");
        }

        int restantes = grupo.getNumeroPersonas();

        for (Mesa mesa : mesas) {

            if (restantes <= 0) break;

            // 🔥 SOLO USAR MESAS LIBRES
            if (!"LIBRE".equalsIgnoreCase(mesa.getEstado())) {
                continue;
            }

            int libres = mesa.getCapacidad();

            int asignar = Math.min(libres, restantes);

            AsignacionMesa a = new AsignacionMesa();
            a.setGrupo(grupo);
            a.setMesa(mesa);
            a.setPersonasAsignadas(asignar);

            repo.save(a);

            // 🔥 IMPORTANTE: marcar mesa como ocupada
            mesa.setEstado("OCUPADA");
            mesaRepository.save(mesa);

            restantes -= asignar;
        }

        if (restantes > 0) {
            throw new IllegalStateException(
                    "No hay suficientes mesas libres para este grupo");
        }
    }

    public boolean yaAsignado(Long grupoId) {
        return repo.existsByGrupoId(grupoId);
    }
}