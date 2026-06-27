package com.bfhl.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Uniform error envelope returned when any exception is caught.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @JsonProperty("is_success")
    private Boolean isSuccess;

    @JsonProperty("message")
    private String message;
}
