package com.prueba.model.repository;

import com.prueba.model.entity.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
    @Query("SELECT r FROM Respuesta r JOIN r.opcion o JOIN r.pregunta p JOIN p.examen e WHERE o.esCorrecta = true AND r.estudiante.id = :estudianteId AND e.id = :examenId")
    List<Respuesta> findCorrectRespuestasByEstudianteIdAndExamenId(Long estudianteId, Long examenId);
}
