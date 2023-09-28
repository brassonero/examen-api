package com.prueba.constants;

public class Constants {
    private Constants() {}

    public static final String STUDENT_NOT_FOUND = "Estudiante no encontrado";
    public static final String EXAM_NOT_FOUND = "Examen no encontrado";

    // API CONSTANTS
    public static final String BASE_PATH = "${constants.api.url.base.path}";
    public static final String SPECIFICPATHS_SAVE_EXAM = "${constants.api.uri.save.exam}";
    public static final String SPECIFICPATHS_SAVE_STUDENT = "${constants.api.uri.save.student}";
    public static final String SPECIFICPATHS_SAVE_ASSIGN = "${constants.api.uri.save.assign}";
    public static final String SPECIFICPATHS_SAVE_ANSWERS = "${constants.api.uri.save.answers}";
    public static final String SPECIFICPATHS_GET_GRADE = "${constants.api.uri.get.grade}";

    // SWAGGER CONSTANTS
    public static final String OP_SAVE_EXAM = "Crea un nuevo examen";
    public static final String OP_SAVE_STUDENT = "Crea un nuevo estudiante";
    public static final String OP_SAVE_ASSIGN = "Asigna un examen a un estudiante";
    public static final String OP_SAVE_ANSWERS = "Agrega las respuestas de un estudiante para un examen";
    public static final String OP_GET_GRADE = "Obtiene la calificación de un estudiante en un examen";
}
