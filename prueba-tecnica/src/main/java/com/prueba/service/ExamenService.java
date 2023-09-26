package com.prueba.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;

import com.prueba.model.entity.*;
import com.prueba.model.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
public class ExamenService {
    @Autowired
    private ExamenRepository examenRepository;
    @Autowired
    private EstudianteRepository estudianteRepository;
    @Autowired
    private AsignacionRepository asignacionRepository;
    @Autowired
    private RespuestaRepository respuestaRepository;
    @Autowired
    private OpcionRepository opcionRepository;

    public Examen crearExamen(Examen examen) {
        int numeroDePreguntas = examen.getPreguntas().size();
        if (numeroDePreguntas == 0) throw new IllegalArgumentException("El examen debe tener al menos una pregunta.");

        int puntajeBase = 100 / numeroDePreguntas;
        int puntosExtras = 100 % numeroDePreguntas;

        List<Pregunta> preguntas = examen.getPreguntas();
        for (Pregunta pregunta : preguntas) {
            pregunta.setExamen(examen);
            pregunta.setPuntaje(puntajeBase);
            for (Opcion opcion : pregunta.getOpciones()) {
                opcion.setPregunta(pregunta);
            }
        }

        Random random = new Random();
        for (int i = 0; i < puntosExtras; i++) {
            int index = random.nextInt(preguntas.size());
            Pregunta pregunta = preguntas.get(index);
            pregunta.setPuntaje(pregunta.getPuntaje() + 1);
        }

        int totalPuntos = preguntas.stream().mapToInt(Pregunta::getPuntaje).sum();
        if (totalPuntos != 100) throw new IllegalArgumentException("Los puntajes de las preguntas deben sumar 100.");

        LocalDateTime now = LocalDateTime.now();
        ZoneId bogotaZoneId = ZoneId.of("America/Bogota");
        ZonedDateTime bogotaZonedDateTime = now.atZone(bogotaZoneId);
        examen.setFechaExamen(bogotaZonedDateTime.toLocalDateTime());

        return examenRepository.save(examen);
    }

    public Asignacion asignarExamen(Long estudianteId, Long examenId) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        Examen examen = examenRepository.findById(examenId)
                .orElseThrow(() -> new RuntimeException("Examen no encontrado"));

        Asignacion asignacion = new Asignacion();
        asignacion.setEstudiante(estudiante);
        asignacion.setExamen(examen);
        return asignacionRepository.save(asignacion);
    }

    public void guardarRespuestas(Long estudianteId, Long examenId, List<Long> opcionesIds) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado"));

        for (Long opcionId : opcionesIds) {
            Opcion opcion = opcionRepository.findById(opcionId)
                    .orElseThrow(() -> new EntityNotFoundException("Opción no encontrada"));

            Respuesta respuesta = new Respuesta();
            respuesta.setEstudiante(estudiante);
            respuesta.setOpcion(opcion);

            respuestaRepository.save(respuesta);
        }
    }
}
