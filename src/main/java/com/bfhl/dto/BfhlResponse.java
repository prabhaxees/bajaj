package com.bfhl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO — returned for every POST /bfhl call.
 * Jackson field-visibility is configured globally (application.properties)
 * so @JsonProperty on private fields is respected directly.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BfhlResponse {

    @JsonProperty("is_success")
    private Boolean isSuccess;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("roll_number")
    private String rollNumber;

    @JsonProperty("odd_numbers")
    private List<String> oddNumbers;

    @JsonProperty("even_numbers")
    private List<String> evenNumbers;

    @JsonProperty("alphabets")
    private List<String> alphabets;

    @JsonProperty("special_characters")
    private List<String> specialCharacters;

    /** Sum of all numeric tokens (as a String). */
    @JsonProperty("sum")
    private String sum;

    /**
     * All alpha characters extracted from alphabet tokens,
     * reversed, with alternating-caps applied (index 0 = uppercase).
     */
    @JsonProperty("concat_string")
    private String concatString;
}
