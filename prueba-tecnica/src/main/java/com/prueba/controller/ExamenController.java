package com.prueba.controller;

import com.prueba.model.dto.CalificacionResponse;
import com.prueba.model.entity.*;
import com.prueba.model.repository.EstudianteRepository;
import com.prueba.service.ExamenService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.prueba.constants.Constants.*;

@RestController
@AllArgsConstructor
@RequestMapping(BASE_PATH)
public class ExamenController {
    private final ExamenService examenService;
    private final EstudianteRepository estudianteRepository;

    @Operation(summary = OP_SAVE_EXAM)
    @PostMapping(value = SPECIFICPATHS_SAVE_EXAM, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Examen> createExam(@RequestBody Examen examen) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examenService.crearExamen(examen));
    }

    @Operation(summary = OP_SAVE_STUDENT)
    @PostMapping(value = SPECIFICPATHS_SAVE_STUDENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Estudiante> createStudent(@RequestBody Estudiante estudiante) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estudianteRepository.save(estudiante));
    }

    @Operation(summary = OP_SAVE_ASSIGN)
    @PostMapping(value = SPECIFICPATHS_SAVE_ASSIGN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Asignacion> assignExam(
            @RequestParam Long idEstudiante,
            @RequestParam Long idExamen) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examenService.asignarExamen(idEstudiante, idExamen));
    }

    @Operation(summary = OP_SAVE_ANSWERS)
    @PostMapping(value = SPECIFICPATHS_SAVE_ANSWERS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> collectAnswers(
            @RequestParam Long idEstudiante,
            @RequestParam Long idExamen,
            @RequestBody List<Long> opcionesIds) {
        examenService.guardarRespuestas(idEstudiante, idExamen, opcionesIds);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = OP_GET_GRADE)
    @GetMapping(value = SPECIFICPATHS_GET_GRADE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CalificacionResponse> getGrade(
            @RequestParam Long idEstudiante,
            @RequestParam Long idExamen) {
        CalificacionResponse calificacionResponse = examenService.calcularPuntaje(idEstudiante, idExamen);
        return ResponseEntity.ok(calificacionResponse);
    }
}
