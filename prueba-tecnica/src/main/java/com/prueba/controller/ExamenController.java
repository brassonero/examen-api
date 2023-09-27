package com.prueba.controller;

import com.prueba.model.dto.CalificacionResponse;
import com.prueba.model.entity.*;
import com.prueba.model.repository.EstudianteRepository;
import com.prueba.service.ExamenService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ExamenController {
    private final ExamenService examenService;
    private final EstudianteRepository estudianteRepository;

    @Operation(summary = "Crea un nuevo examen")
    @PostMapping("/examenes")
    public ResponseEntity<Examen> crearExamen(@RequestBody Examen examen) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examenService.crearExamen(examen));
    }

    @Operation(summary = "Crea un nuevo estudiante")
    @PostMapping("/estudiantes")
    public ResponseEntity<Estudiante> crearEstudiante(@RequestBody Estudiante estudiante) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estudianteRepository.save(estudiante));
    }

    @Operation(summary = "Asigna un examen a un estudiante")
    @PostMapping("/asignaciones")
    public ResponseEntity<Asignacion> asignarExamen(@RequestParam Long estudianteId, @RequestParam Long examenId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examenService.asignarExamen(estudianteId, examenId));
    }

    @Operation(summary = "Agrega las respuestas de un estudiante para un examen")
    @PostMapping("/respuestas")
    public ResponseEntity<Void> recopilarRespuestas(@RequestParam Long estudianteId, @RequestParam Long examenId, @RequestBody List<Long> opcionesIds) {
        examenService.guardarRespuestas(estudianteId, examenId, opcionesIds);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Obtiene la calificación de un estudiante en un examen")
    @GetMapping("/calificacion")
    public ResponseEntity<CalificacionResponse> obtenerCalificacion(@RequestParam Long estudianteId, @RequestParam Long examenId) {
        CalificacionResponse calificacionResponse = examenService.calcularPuntaje(estudianteId, examenId);
        return ResponseEntity.ok(calificacionResponse);
    }
}
