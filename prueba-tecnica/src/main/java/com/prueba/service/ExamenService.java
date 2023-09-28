package com.prueba.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.prueba.model.dto.*;
import com.prueba.model.entity.*;
import com.prueba.model.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static com.prueba.constants.Constants.*;
import static com.prueba.utils.Util.convertToExamenEntity;
import static com.prueba.utils.Util.convertToExamenResponse;

@Slf4j
@Service
@AllArgsConstructor
public class ExamenService {
    private final ExamenRepository examenRepository;
    private final EstudianteRepository estudianteRepository;
    private final AsignacionRepository asignacionRepository;
    private final RespuestaRepository respuestaRepository;
    private final OpcionRepository opcionRepository;

    public ExamenResponse crearExamen(ExamenRequest request) {

        Examen examen = convertToExamenEntity(request);

        int numeroDePreguntas = Optional.ofNullable(examen.getPreguntas()).orElse(Collections.emptyList()).size();

        if (numeroDePreguntas == 0){
            throw new IllegalArgumentException("El examen debe tener al menos una pregunta.");
        }

        int puntajeBase = 100 / numeroDePreguntas;
        int puntosExtras = 100 % numeroDePreguntas;

        Optional.ofNullable(examen.getPreguntas()).ifPresent(preguntas -> preguntas.forEach(pregunta -> {
            pregunta.setExamen(examen);
            pregunta.setPuntaje(puntajeBase);
            Optional.ofNullable(pregunta.getOpciones()).ifPresent(opciones -> opciones.forEach(opcion -> opcion.setPregunta(pregunta)));
        }));

        SecureRandom secureRandom = new SecureRandom();
        IntStream.range(0, puntosExtras).forEach(i -> {
            int index = secureRandom.nextInt(Optional.ofNullable(examen.getPreguntas()).orElse(Collections.emptyList()).size());
            Pregunta pregunta = examen.getPreguntas().get(index);
            pregunta.setPuntaje(pregunta.getPuntaje() + 1);
        });

        int totalPuntos = Optional.ofNullable(examen.getPreguntas())
                .orElse(Collections.emptyList())
                .stream()
                .mapToInt(Pregunta::getPuntaje)
                .sum();

        if (totalPuntos != 100) {
            throw new IllegalArgumentException("Los puntajes de las preguntas deben sumar 100.");
        }

        LocalDateTime now = LocalDateTime.now();
        ZoneId bogotaZoneId = ZoneId.of("America/Bogota");
        ZonedDateTime bogotaZonedDateTime = now.atZone(bogotaZoneId);
        examen.setFechaExamen(bogotaZonedDateTime.toLocalDateTime());

        Examen savedExamen = examenRepository.save(examen);
        return convertToExamenResponse(savedExamen);
    }

    public EstudianteResponse crearEstudiante(EstudianteRequest request) {

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(request.getNombre());
        estudiante.setEdad(request.getEdad());
        estudiante.setCiudad(request.getCiudad());
        estudiante.setZonaHoraria(request.getZonaHoraria());

        Estudiante savedEstudiante = estudianteRepository.save(estudiante);

        return EstudianteResponse.builder()
                .id(savedEstudiante.getId())
                .nombre(savedEstudiante.getNombre())
                .build();
    }

    public AsignacionResponse asignarExamen(Long idEstudiante, Long idExamen) {

        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new RuntimeException(STUDENT_NOT_FOUND));
        Examen examen = examenRepository.findById(idExamen)
                .orElseThrow(() -> new RuntimeException(EXAM_NOT_FOUND));

        Asignacion asignacion = new Asignacion();
        asignacion.setEstudiante(estudiante);
        asignacion.setExamen(examen);

        Asignacion savedAssign = asignacionRepository.save(asignacion);

        return AsignacionResponse.builder()
                .id(savedAssign.getId())
                .examen(ExamenResponse.builder()
                        .id(savedAssign.getExamen().getId())
                        .fechaExamen(savedAssign.getExamen().getFechaExamen())
                        .build())
                .estudiante(EstudianteResponse.builder()
                        .id(savedAssign.getExamen().getId())
                        .nombre(savedAssign.getEstudiante().getNombre())
                        .build())
                .build();
    }

    public void guardarRespuestas(Long idEstudiante, Long idExamen, List<Long> idOpciones) {

        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new EntityNotFoundException(STUDENT_NOT_FOUND));

        if (!examenRepository.existsById(idExamen)) {
            throw new EntityNotFoundException(EXAM_NOT_FOUND);
        }

        idOpciones.forEach(idOpcion -> {
            Opcion opcion = opcionRepository.findById(idOpcion)
                    .orElseThrow(() -> new EntityNotFoundException("Opción no encontrada"));

            if (!opcion.getPregunta().getExamen().getId().equals(idExamen)) {
                throw new IllegalArgumentException("El idOpcion " + idOpcion + " no pertenece al idExamen " + idExamen);
            }

            Respuesta respuesta = new Respuesta();
            respuesta.setEstudiante(estudiante);
            respuesta.setOpcion(opcion);
            respuesta.setPregunta(opcion.getPregunta());

            respuestaRepository.save(respuesta);
        });
    }

    public CalificacionResponse calcularPuntaje(Long idEstudiante, Long idExamen) {

        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new EntityNotFoundException(STUDENT_NOT_FOUND));

        Examen examen = examenRepository.findById(idExamen)
                .orElseThrow(() -> new EntityNotFoundException(EXAM_NOT_FOUND));

        List<Respuesta> respuestasCorrectas = respuestaRepository.findCorrectRespuestasByEstudianteIdAndExamenId(idEstudiante, idExamen);

        int totalPuntaje = respuestasCorrectas.stream()
                .mapToInt(respuesta -> respuesta.getPregunta().getPuntaje())
                .sum();

        return CalificacionResponse.builder()
                .idEstudiante(idEstudiante)
                .idExamen(examen.getId())
                .nombre(estudiante.getNombre())
                .calificacion(totalPuntaje)
                .build();
    }
}
