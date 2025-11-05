package com.rs.reserva_simple.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Estructura para las respuestas de error en toda la aplicación
 *
 * @JsonInclude(JsonInclude.Include.NON_NULL): Solo incluye campos no nulos en el JSON
 * Esto hace que la respuesta sea más limpia y no incluya campos vacíos innecesarios
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Marca temporal de cuándo ocurrió el error
     * Formato: "yyyy-MM-dd'T'HH:mm:ss"
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * Código de estado HTTP (400, 404, 500, etc.)
     */
    private Integer status;

    /**
     * Descripción textual del código de estado (Bad Request, Not Found, etc.)
     */
    private String error;

    /**
     * Mensaje descriptivo del error para el usuario
     */
    private String message;

    /**
     * Ruta del endpoint donde ocurrió el error
     */
    private String path;

    /**
     * Mapa de errores de validación (campo -> mensaje de error)
     * Solo se incluye cuando hay errores de validación de Bean Validation
     */
    private Map<String, String> validationErrors;

    /**
     * Detalles adicionales del error (opcional)
     * Usado para información técnica adicional
     */
    private String details;
}
