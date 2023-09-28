package com.prueba.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaDTO {
    private String enunciado;
    private Integer puntaje;
    private List<OpcionDTO> opciones;
}
