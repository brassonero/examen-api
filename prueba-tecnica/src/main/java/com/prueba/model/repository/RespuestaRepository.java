package com.prueba.model.repository;

import com.prueba.model.entity.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
    List<Respuesta> findByEstudianteIdAndOpcionPreguntaExamenId(Long estudianteId, Long examenId);
}
