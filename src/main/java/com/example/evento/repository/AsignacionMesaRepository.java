package com.example.evento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.evento.entity.AsignacionMesa;

public interface AsignacionMesaRepository extends JpaRepository<AsignacionMesa, Long> {

    boolean existsByGrupoId(Long grupoId);

    boolean existsByMesaId(Long mesaId);

    List<AsignacionMesa> findByGrupoId(Long grupoId);

    List<AsignacionMesa> findByMesaId(Long mesaId);

    @Query("""
                SELECT COALESCE(SUM(a.personasAsignadas), 0)
                FROM AsignacionMesa a
                WHERE a.mesa.id = :mesaId
            """)
    int sumPersonasByMesaId(@Param("mesaId") Long mesaId);
}
