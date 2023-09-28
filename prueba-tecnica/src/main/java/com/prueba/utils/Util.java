package com.prueba.utils;

import com.prueba.model.dto.ExamenRequest;
import com.prueba.model.dto.ExamenResponse;
import com.prueba.model.dto.OpcionDTO;
import com.prueba.model.dto.PreguntaDTO;
import com.prueba.model.entity.Examen;
import com.prueba.model.entity.Opcion;
import com.prueba.model.entity.Pregunta;

import java.util.List;
import java.util.stream.Collectors;

public class Util {
    private Util() {}

    public static Examen convertToExamenEntity(ExamenRequest request) {
        Examen examen = new Examen();
        examen.setFechaExamen(request.getFechaExamen());
        List<Pregunta> preguntas = request.getPreguntas().stream()
                .map(Util::convertToPreguntaEntity).collect(Collectors.toList());
        examen.setPreguntas(preguntas);
        return examen;
    }

    private static Pregunta convertToPreguntaEntity(PreguntaDTO dto) {
        Pregunta pregunta = new Pregunta();
        pregunta.setEnunciado(dto.getEnunciado());
        pregunta.setPuntaje(dto.getPuntaje());
        List<Opcion> opciones = dto.getOpciones().stream()
                .map(Util::convertToOpcionEntity).collect(Collectors.toList());
        pregunta.setOpciones(opciones);
        return pregunta;
    }

    private static Opcion convertToOpcionEntity(OpcionDTO dto) {
        Opcion opcion = new Opcion();
        opcion.setTexto(dto.getTexto());
        opcion.setEsCorrecta(dto.getEsCorrecta());
        return opcion;
    }
}
