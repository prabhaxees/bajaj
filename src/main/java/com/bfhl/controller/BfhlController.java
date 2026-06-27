package com.bfhl.controller;

import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;
import com.bfhl.service.BfhlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the BFHL challenge endpoint.
 *
 * <p>Route: POST /bfhl
 * <p>Success status: 200 OK
 */
@RestController
@RequestMapping("/bfhl")
@RequiredArgsConstructor
public class BfhlController {

    private final BfhlService bfhlService;

    /**
     * Classifies the incoming data array and returns categorised results.
     *
     * @param request JSON body — must include a non-null "data" array
     * @return {@link BfhlResponse} with HTTP 200 on success
     */
    @PostMapping
    public ResponseEntity<BfhlResponse> process(@Valid @RequestBody BfhlRequest request) {
        BfhlResponse response = bfhlService.process(request);
        return ResponseEntity.ok(response);
    }
}
