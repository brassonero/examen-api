package com.prueba.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "Examen")
public class Examen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaExamen;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "examen")
    @JsonManagedReference
    private List<Pregunta> preguntas = new ArrayList<>();
}
