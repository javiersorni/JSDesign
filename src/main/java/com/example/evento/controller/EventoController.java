package com.example.evento.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.evento.entity.Evento;
import com.example.evento.entity.Grupo;
import com.example.evento.entity.Mesa;
import com.example.evento.repository.EventoRepository;
import com.example.evento.repository.GrupoRepository;
import com.example.evento.repository.MesaRepository;

@Controller
@RequestMapping("/admin/eventos")
public class EventoController {

    private final EventoRepository eventoRepository;
    private final MesaRepository mesaRepository;
    private final GrupoRepository grupoRepository;

    public EventoController(EventoRepository eventoRepository,
            MesaRepository mesaRepository, GrupoRepository grupoRepository) {
        this.eventoRepository = eventoRepository;
        this.mesaRepository = mesaRepository;
        this.grupoRepository = grupoRepository;
    }

    // =========================
    // LISTAR EVENTOS
    // =========================
    @GetMapping
    public String listarEventos(Model model) {

        model.addAttribute("eventos", eventoRepository.findAll());

        return "admin/listar-eventos";
    }

    // =========================
    // FORM NUEVO EVENTO
    // =========================
    @GetMapping("/nuevo")
    public String nuevoEvento(Model model) {

        model.addAttribute("evento", new Evento());

        return "admin/crear-evento";
    }

    // =========================
    // CREAR EVENTO
    // =========================
    @PostMapping
    public String crearEvento(@ModelAttribute Evento evento) {

        Evento eventoGuardado = eventoRepository.save(evento);

        crearMesas(eventoGuardado);

        return "redirect:/admin/eventos";
    }

    // =========================
    // DETALLE EVENTO
    // =========================
    @GetMapping("/{id}")
    public String detalleEvento(@PathVariable Long id, Model model) {

        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        List<Mesa> mesas = mesaRepository.findByEventoId(id);

        List<Grupo> grupos = grupoRepository.findByEventoId(id);

        model.addAttribute("evento", evento);
        model.addAttribute("mesas", mesas);
        model.addAttribute("grupos", grupos);

        return "admin/detalle-evento";
    }

    // =========================
    // FORM EDITAR
    // =========================
    @GetMapping("/editar/{id}")
    public String editarEvento(@PathVariable Long id, Model model) {

        model.addAttribute("evento", obtenerEvento(id));

        return "admin/editar-evento";
    }

    // =========================
    // ACTUALIZAR EVENTO
    // =========================
    @PostMapping("/actualizar/{id}")
    public String actualizarEvento(@PathVariable Long id,
            @ModelAttribute Evento eventoActualizado) {

        Evento evento = obtenerEvento(id);

        evento.setNombre(eventoActualizado.getNombre());
        evento.setFechaEvento(eventoActualizado.getFechaEvento());

        // 🔥 sincronizar mesas si cambia cantidad
        sincronizarMesas(evento, eventoActualizado.getCantidadMesas());

        evento.setCantidadMesas(eventoActualizado.getCantidadMesas());

        eventoRepository.save(evento);

        return "redirect:/admin/eventos";
    }

    // =========================
    // ELIMINAR EVENTO
    // =========================
    @PostMapping("/eliminar/{id}")
    public String eliminarEvento(@PathVariable Long id) {

        if (!eventoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado");
        }

        mesaRepository.deleteByEventoId(id);
        eventoRepository.deleteById(id);

        return "redirect:/admin/eventos";
    }

    // =========================
    // HELPERS PRIVADOS
    // =========================

    private Evento obtenerEvento(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado"));
    }

    private void crearMesas(Evento evento) {
        for (int i = 1; i <= evento.getCantidadMesas(); i++) {
            Mesa mesa = new Mesa();
            mesa.setNumeroMesa(i);
            mesa.setCapacidad(6);
            mesa.setEstado("LIBRE");
            mesa.setEvento(evento);
            mesaRepository.save(mesa);
        }
    }

    private void sincronizarMesas(Evento evento, int nuevasMesas) {

        List<Mesa> actuales = mesaRepository.findByEventoId(evento.getId());

        int totalActual = actuales.size();

        // 🔼 si hay más mesas nuevas, crear
        if (nuevasMesas > totalActual) {
            for (int i = totalActual + 1; i <= nuevasMesas; i++) {
                Mesa mesa = new Mesa();
                mesa.setNumeroMesa(i);
                mesa.setCapacidad(6);
                mesa.setEstado("LIBRE");
                mesa.setEvento(evento);
                mesaRepository.save(mesa);
            }
        }

        // 🔽 si hay menos mesas, eliminar sobrantes
        if (nuevasMesas < totalActual) {
            List<Mesa> aEliminar = actuales.subList(nuevasMesas, totalActual);
            mesaRepository.deleteAll(aEliminar);
        }
    }
}