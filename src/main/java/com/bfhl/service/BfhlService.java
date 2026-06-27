package com.bfhl.service;

import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;

/**
 * Service contract for the BFHL processing logic.
 */
public interface BfhlService {

    /**
     * Processes the incoming data array and classifies each element.
     *
     * @param request validated request containing the data list
     * @return fully-populated {@link BfhlResponse}
     */
    BfhlResponse process(BfhlRequest request);
}
