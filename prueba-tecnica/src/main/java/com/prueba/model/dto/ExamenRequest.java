package com.prueba.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamenRequest {
    private LocalDateTime fechaExamen;
    private List<PreguntaDTO> preguntas;
}
