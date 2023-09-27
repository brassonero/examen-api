package com.prueba.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CalificacionResponse {
    private Long idExamen;
    private Long idEstudiante;
    private String nombre;
    private Integer calificacion;
}
