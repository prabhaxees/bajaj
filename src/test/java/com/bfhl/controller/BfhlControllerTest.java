package com.bfhl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("POST /bfhl — integration tests")
class BfhlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================================================================
    // Happy path
    // =========================================================================

    @Test
    @DisplayName("Example A → HTTP 200 and correct JSON fields")
    void testExampleA() throws Exception {
        String body = objectMapper.writeValueAsString(
                Map.of("data", List.of("a", "1", "334", "4", "R", "$")));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.odd_numbers[0]").value("1"))
                .andExpect(jsonPath("$.even_numbers[0]").value("334"))
                .andExpect(jsonPath("$.even_numbers[1]").value("4"))
                .andExpect(jsonPath("$.alphabets[0]").value("A"))
                .andExpect(jsonPath("$.alphabets[1]").value("R"))
                .andExpect(jsonPath("$.special_characters[0]").value("$"))
                .andExpect(jsonPath("$.sum").value("339"))
                .andExpect(jsonPath("$.concat_string").value("Ra"))
                .andExpect(jsonPath("$.user_id").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty())
                .andExpect(jsonPath("$.roll_number").isNotEmpty());
    }

    @Test
    @DisplayName("Example B → HTTP 200 and correct JSON fields")
    void testExampleB() throws Exception {
        String body = objectMapper.writeValueAsString(
                Map.of("data", List.of("2", "a", "y", "4", "&", "-", "*", "5", "92", "b")));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.odd_numbers[0]").value("5"))
                .andExpect(jsonPath("$.even_numbers[0]").value("2"))
                .andExpect(jsonPath("$.alphabets[0]").value("A"))
                .andExpect(jsonPath("$.alphabets[1]").value("Y"))
                .andExpect(jsonPath("$.alphabets[2]").value("B"))
                .andExpect(jsonPath("$.special_characters[0]").value("&"))
                .andExpect(jsonPath("$.sum").value("103"))
                .andExpect(jsonPath("$.concat_string").value("ByA"));
    }

    @Test
    @DisplayName("Example C → HTTP 200 with multi-char alphabets and concat_string=EoDdCbAa")
    void testExampleC() throws Exception {
        String body = objectMapper.writeValueAsString(
                Map.of("data", List.of("A", "ABCD", "DOE")));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.odd_numbers").isEmpty())
                .andExpect(jsonPath("$.even_numbers").isEmpty())
                .andExpect(jsonPath("$.alphabets[0]").value("A"))
                .andExpect(jsonPath("$.alphabets[1]").value("ABCD"))
                .andExpect(jsonPath("$.alphabets[2]").value("DOE"))
                .andExpect(jsonPath("$.special_characters").isEmpty())
                .andExpect(jsonPath("$.sum").value("0"))
                .andExpect(jsonPath("$.concat_string").value("EoDdCbAa"));
    }

    @Test
    @DisplayName("Empty data array → HTTP 200 with all empty arrays")
    void testEmptyData() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("data", List.of()));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.sum").value("0"))
                .andExpect(jsonPath("$.concat_string").value(""));
    }

    // =========================================================================
    // Error / validation scenarios
    // =========================================================================

    @Test
    @DisplayName("Missing 'data' field → HTTP 400 with is_success=false")
    void testMissingDataField() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("Malformed JSON → HTTP 400 with is_success=false")
    void testMalformedJson() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{not_valid_json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }
}
