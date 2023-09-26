package com.prueba.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "Pregunta")
public class Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String enunciado;
    private Integer puntaje;
    @ManyToOne
    @JoinColumn(name = "examen_id")
    @JsonBackReference
    private Examen examen;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pregunta")
    @JsonManagedReference
    private List<Opcion> opciones = new ArrayList<>();
}
