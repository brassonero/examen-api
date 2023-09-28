package com.prueba.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotNull(message = "La edad es obligatoria")
    private Integer edad;
    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;
    private String zonaHoraria;
}
