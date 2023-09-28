package com.prueba.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AsignacionResponse {
    private Long id;
    private ExamenResponse examen;
    private EstudianteResponse estudiante;
}
