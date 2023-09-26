package com.prueba.controller;

import com.prueba.model.entity.*;
import com.prueba.model.repository.EstudianteRepository;
import com.prueba.service.ExamenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExamenController {
    @Autowired
    private ExamenService examenService;
    @Autowired
    private EstudianteRepository estudianteRepository;

    @PostMapping("/examenes")
    public ResponseEntity<Examen> crearExamen(@RequestBody Examen examen) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examenService.crearExamen(examen));
    }

    @PostMapping("/estudiantes")
    public ResponseEntity<Estudiante> crearEstudiante(@RequestBody Estudiante estudiante) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estudianteRepository.save(estudiante));
    }

    @PostMapping("/asignaciones")
    public ResponseEntity<Asignacion> asignarExamen(@RequestParam Long estudianteId, @RequestParam Long examenId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examenService.asignarExamen(estudianteId, examenId));
    }

    @PostMapping("/respuestas")
    public ResponseEntity<Void> recopilarRespuestas(@RequestParam Long estudianteId, @RequestParam Long examenId, @RequestBody List<Long> opcionesIds) {
        examenService.guardarRespuestas(estudianteId, examenId, opcionesIds);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
