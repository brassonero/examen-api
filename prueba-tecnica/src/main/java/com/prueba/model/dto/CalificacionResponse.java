package com.prueba.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CalificacionResponse {
    private Long estudianteId;
    private String estudianteNombre;
    private Integer totalPuntaje;
}
