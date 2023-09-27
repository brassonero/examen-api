package com.prueba.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
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
    @JoinColumn(name = "id_examen")
    @JsonBackReference
    private Examen examen;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL)
    private List<Opcion> opciones;
}
