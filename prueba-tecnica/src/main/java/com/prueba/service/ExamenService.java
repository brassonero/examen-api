package com.prueba.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;

import com.prueba.model.dto.CalificacionResponse;
import com.prueba.model.entity.*;
import com.prueba.model.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@AllArgsConstructor
public class ExamenService {
    private final ExamenRepository examenRepository;
    private final EstudianteRepository estudianteRepository;
    private final AsignacionRepository asignacionRepository;
    private final RespuestaRepository respuestaRepository;
    private final OpcionRepository opcionRepository;
    private static final Random RANDOM = new Random();
    private static final String STUDENT_NOT_FOUND = "Estudiante no encontrado";

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

        for (int i = 0; i < puntosExtras; i++) {
            int index = RANDOM.nextInt(preguntas.size());
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
                .orElseThrow(() -> new RuntimeException(STUDENT_NOT_FOUND));
        Examen examen = examenRepository.findById(examenId)
                .orElseThrow(() -> new RuntimeException("Examen no encontrado"));

        Asignacion asignacion = new Asignacion();
        asignacion.setEstudiante(estudiante);
        asignacion.setExamen(examen);
        return asignacionRepository.save(asignacion);
    }

    public void guardarRespuestas(Long estudianteId, Long examenId, List<Long> opcionesIds) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new EntityNotFoundException(STUDENT_NOT_FOUND));

        if (!examenRepository.existsById(examenId)) {
            throw new EntityNotFoundException("Examen no encontrado");
        }

        for (Long opcionId : opcionesIds) {
            Opcion opcion = opcionRepository.findById(opcionId)
                    .orElseThrow(() -> new EntityNotFoundException("Opción no encontrada"));

            if (!opcion.getPregunta().getExamen().getId().equals(examenId)) {
                throw new IllegalArgumentException("El opcionId " + opcionId + " no pertenece al examenId " + examenId);
            }

            Respuesta respuesta = new Respuesta();
            respuesta.setEstudiante(estudiante);
            respuesta.setOpcion(opcion);
            respuesta.setPregunta(opcion.getPregunta());

            respuestaRepository.save(respuesta);
        }
    }

    public CalificacionResponse calcularPuntaje(Long estudianteId, Long examenId) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new EntityNotFoundException(STUDENT_NOT_FOUND));

        List<Respuesta> respuestasCorrectas = respuestaRepository.findCorrectRespuestasByEstudianteIdAndExamenId(estudianteId, examenId);

        int totalPuntaje = respuestasCorrectas.stream()
                .mapToInt(respuesta -> respuesta.getPregunta().getPuntaje())
                .sum();

        CalificacionResponse calificacionResponse = new CalificacionResponse();
        calificacionResponse.setEstudianteId(estudianteId);
        calificacionResponse.setEstudianteNombre(estudiante.getNombre());
        calificacionResponse.setTotalPuntaje(totalPuntaje);

        return calificacionResponse;
    }
}
