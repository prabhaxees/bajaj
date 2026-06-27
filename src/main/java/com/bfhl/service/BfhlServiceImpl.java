package com.bfhl.service;

import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of {@link BfhlService}.
 *
 * <p>Classification rules:
 * <ul>
 *   <li>A token is a <b>number</b>  if it matches {@code \d+} (one or more digits only).</li>
 *   <li>A token is an <b>alphabet</b> if it matches {@code [a-zA-Z]+} (letters only).</li>
 *   <li>Everything else is a <b>special character</b> token.</li>
 * </ul>
 *
 * <p>concat_string logic:
 * <ol>
 *   <li>Collect every individual character from all alphabet tokens (in input order).</li>
 *   <li>Reverse that character list.</li>
 *   <li>Apply alternating-caps starting with UPPERCASE at index 0.</li>
 * </ol>
 */
@Service
public class BfhlServiceImpl implements BfhlService {

    @Value("${user.id}")
    private String userId;

    @Value("${user.email}")
    private String email;

    @Value("${user.roll_number}")
    private String rollNumber;

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    @Override
    public BfhlResponse process(BfhlRequest request) {
        List<String> data = request.getData();

        List<String> evenNumbers     = new ArrayList<>();
        List<String> oddNumbers      = new ArrayList<>();
        List<String> alphabets       = new ArrayList<>();
        List<String> specialChars    = new ArrayList<>();
        List<Character> allAlphaChars = new ArrayList<>();
        long sum = 0;

        for (String token : data) {
            if (token == null) continue;

            if (isNumeric(token)) {
                long num = Long.parseLong(token);
                sum += num;
                if (num % 2 == 0) {
                    evenNumbers.add(token);
                } else {
                    oddNumbers.add(token);
                }
            } else if (isAlphabetic(token)) {
                alphabets.add(token.toUpperCase());
                for (char c : token.toCharArray()) {
                    allAlphaChars.add(c);
                }
            } else {
                specialChars.add(token);
            }
        }

        return BfhlResponse.builder()
                .isSuccess(true)
                .userId(userId)
                .email(email)
                .rollNumber(rollNumber)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialChars)
                .sum(String.valueOf(sum))
                .concatString(buildConcatString(allAlphaChars))
                .build();
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Returns true only if the token consists entirely of ASCII digits (0-9).
     * A leading '-' is NOT treated as part of a number (it becomes a special char).
     */
    boolean isNumeric(String token) {
        return token != null && !token.isEmpty() && token.matches("\\d+");
    }

    /**
     * Returns true only if the token consists entirely of ASCII letters (a-z, A-Z).
     * Handles multi-character tokens like "ABCD" or "DOE".
     */
    boolean isAlphabetic(String token) {
        return token != null && !token.isEmpty() && token.matches("[a-zA-Z]+");
    }

    /**
     * Builds the alternating-caps string from the collected alpha characters.
     *
     * <p>Steps:
     * <ol>
     *   <li>Reverse the character list.</li>
     *   <li>Even index (0, 2, 4 …) → UPPERCASE.</li>
     *   <li>Odd  index (1, 3, 5 …) → lowercase.</li>
     * </ol>
     */
    String buildConcatString(List<Character> chars) {
        if (chars.isEmpty()) return "";

        List<Character> reversed = new ArrayList<>(chars);
        Collections.reverse(reversed);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < reversed.size(); i++) {
            char c = reversed.get(i);
            sb.append(i % 2 == 0 ? Character.toUpperCase(c) : Character.toLowerCase(c));
        }
        return sb.toString();
    }
}
