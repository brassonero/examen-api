package com.prueba.controller;

import com.prueba.model.dto.*;
import com.prueba.service.ExamenService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.prueba.constants.Constants.*;

@RestController
@AllArgsConstructor
@RequestMapping(BASE_PATH)
public class ExamenController {
    private final ExamenService examenService;

    @Operation(summary = OP_SAVE_EXAM)
    @PostMapping(value = SPECIFICPATHS_SAVE_EXAM, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExamenResponse> createExam(@RequestBody ExamenRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examenService.crearExamen(request));
    }

    @Operation(summary = OP_SAVE_STUDENT)
    @PostMapping(value = SPECIFICPATHS_SAVE_STUDENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EstudianteResponse> createStudent(@Valid @RequestBody EstudianteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examenService.crearEstudiante(request));
    }

    @Operation(summary = OP_SAVE_ASSIGN)
    @PostMapping(value = SPECIFICPATHS_SAVE_ASSIGN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AsignacionResponse> assignExam(
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
