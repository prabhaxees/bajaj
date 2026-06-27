package com.bfhl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO — wraps the incoming "data" array.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BfhlRequest {

    @JsonProperty("data")
    @NotNull(message = "'data' field is required and must not be null")
    private List<String> data;
}
