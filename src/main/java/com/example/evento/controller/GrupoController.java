package com.example.evento.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.evento.entity.Evento;
import com.example.evento.entity.Grupo;
import com.example.evento.entity.Mesa;
import com.example.evento.repository.EventoRepository;
import com.example.evento.repository.GrupoRepository;
import com.example.evento.repository.MesaRepository;
import com.example.evento.service.AsignacionService;

@Controller
@RequestMapping("/admin/grupos")
public class GrupoController {

        private final GrupoRepository grupoRepository;
        private final EventoRepository eventoRepository;
        private final MesaRepository mesaRepository;
        private final AsignacionService asignacionService;

        public GrupoController(
                        GrupoRepository grupoRepository,
                        EventoRepository eventoRepository,
                        MesaRepository mesaRepository,
                        AsignacionService asignacionService) {
                this.grupoRepository = grupoRepository;
                this.eventoRepository = eventoRepository;
                this.mesaRepository = mesaRepository;
                this.asignacionService = asignacionService;
        }

        @GetMapping("/nuevo/{eventoId}")
        public String crearGrupo(@PathVariable Long eventoId, Model model) {

                Evento evento = eventoRepository.findById(eventoId)
                                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

                model.addAttribute("evento", evento);
                model.addAttribute("grupo", new Grupo());

                return "admin/crear-grupo";
        }

        @PostMapping("/guardar/{eventoId}")
        public String guardarGrupo(@PathVariable Long eventoId,
                        @ModelAttribute Grupo grupo) {

                Evento evento = eventoRepository.findById(eventoId)
                                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

                grupo.setEvento(evento);

                grupoRepository.save(grupo);

                return "redirect:/admin/eventos/" + eventoId;
        }

        @GetMapping("/asignar/{grupoId}")
        public String formAsignar(@PathVariable Long grupoId, Model model) {

                Grupo grupo = grupoRepository.findById(grupoId)
                                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

                List<Mesa> mesas = mesaRepository
                                .findByEventoIdOrderByNumeroMesaAsc(grupo.getEvento().getId());

                model.addAttribute("grupo", grupo);
                model.addAttribute("mesas", mesas);

                return "admin/asignar-mesa";
        }

        @PostMapping("/asignar/{grupoId}")
        public String asignarAutomatico(
                        @PathVariable Long grupoId,
                        RedirectAttributes redirectAttributes) {

                try {

                        Grupo grupo = grupoRepository.findById(grupoId)
                                        .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

                        List<Mesa> mesas = mesaRepository
                                        .findByEventoId(grupo.getEvento().getId());

                        asignacionService.asignarGrupo(grupoId, mesas);

                        redirectAttributes.addFlashAttribute(
                                        "success",
                                        "Grupo asignado correctamente");

                } catch (IllegalStateException e) {

                        redirectAttributes.addFlashAttribute(
                                        "error",
                                        e.getMessage());

                } catch (Exception e) {

                        redirectAttributes.addFlashAttribute(
                                        "error",
                                        "Error al asignar el grupo");
                }

                return "redirect:/admin/eventos/" + grupoRepository.findById(grupoId)
                                .orElseThrow()
                                .getEvento()
                                .getId();
        }
}
