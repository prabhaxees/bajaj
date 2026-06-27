package com.bfhl.service;

import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("BfhlServiceImpl — unit tests")
class BfhlServiceImplTest {

    @Autowired
    private BfhlService bfhlService;

    // Direct access to the impl for helper-method tests
    private BfhlServiceImpl serviceImpl;

    @BeforeEach
    void setUp() {
        serviceImpl = new BfhlServiceImpl();
    }

    // =========================================================================
    // Example A  — mixed singles
    // =========================================================================

    @Test
    @DisplayName("Example A: [a, 1, 334, 4, R, $]")
    void testExampleA() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));
        BfhlResponse res = bfhlService.process(req);

        assertThat(res.getIsSuccess()).isTrue();
        assertThat(res.getOddNumbers()).containsExactly("1");
        assertThat(res.getEvenNumbers()).containsExactly("334", "4");
        assertThat(res.getAlphabets()).containsExactly("A", "R");
        assertThat(res.getSpecialCharacters()).containsExactly("$");
        assertThat(res.getSum()).isEqualTo("339");
        assertThat(res.getConcatString()).isEqualTo("Ra");
    }

    // =========================================================================
    // Example B  — multiple specials, multi-number tokens
    // =========================================================================

    @Test
    @DisplayName("Example B: [2, a, y, 4, &, -, *, 5, 92, b]")
    void testExampleB() {
        BfhlRequest req = new BfhlRequest(
                Arrays.asList("2", "a", "y", "4", "&", "-", "*", "5", "92", "b"));
        BfhlResponse res = bfhlService.process(req);

        assertThat(res.getIsSuccess()).isTrue();
        assertThat(res.getOddNumbers()).containsExactly("5");
        assertThat(res.getEvenNumbers()).containsExactly("2", "4", "92");
        assertThat(res.getAlphabets()).containsExactly("A", "Y", "B");
        assertThat(res.getSpecialCharacters()).containsExactly("&", "-", "*");
        assertThat(res.getSum()).isEqualTo("103");
        assertThat(res.getConcatString()).isEqualTo("ByA");
    }

    // =========================================================================
    // Example C  — multi-character alphabet tokens
    // =========================================================================

    @Test
    @DisplayName("Example C: [A, ABCD, DOE] — multi-char alphabets")
    void testExampleC() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("A", "ABCD", "DOE"));
        BfhlResponse res = bfhlService.process(req);

        assertThat(res.getIsSuccess()).isTrue();
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getEvenNumbers()).isEmpty();
        assertThat(res.getAlphabets()).containsExactly("A", "ABCD", "DOE");
        assertThat(res.getSpecialCharacters()).isEmpty();
        assertThat(res.getSum()).isEqualTo("0");
        // chars in order: A A B C D D O E  → reversed: E O D D C B A A
        // alternating:    E o D d C b A a  → "EoDdCbAa"
        assertThat(res.getConcatString()).isEqualTo("EoDdCbAa");
    }

    // =========================================================================
    // Edge cases
    // =========================================================================

    @Test
    @DisplayName("Empty array → all lists empty, sum=0, concatString=''")
    void testEmptyArray() {
        BfhlRequest req = new BfhlRequest(Collections.emptyList());
        BfhlResponse res = bfhlService.process(req);

        assertThat(res.getIsSuccess()).isTrue();
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getEvenNumbers()).isEmpty();
        assertThat(res.getAlphabets()).isEmpty();
        assertThat(res.getSpecialCharacters()).isEmpty();
        assertThat(res.getSum()).isEqualTo("0");
        assertThat(res.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("Only numbers: sum and even/odd split")
    void testOnlyNumbers() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("3", "10", "7", "100"));
        BfhlResponse res = bfhlService.process(req);

        assertThat(res.getOddNumbers()).containsExactly("3", "7");
        assertThat(res.getEvenNumbers()).containsExactly("10", "100");
        assertThat(res.getAlphabets()).isEmpty();
        assertThat(res.getSpecialCharacters()).isEmpty();
        assertThat(res.getSum()).isEqualTo("120");
        assertThat(res.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("Only special chars → all in special_characters list")
    void testOnlySpecials() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("@", "#", "!"));
        BfhlResponse res = bfhlService.process(req);

        assertThat(res.getSpecialCharacters()).containsExactly("@", "#", "!");
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getEvenNumbers()).isEmpty();
        assertThat(res.getAlphabets()).isEmpty();
        assertThat(res.getSum()).isEqualTo("0");
    }

    @Test
    @DisplayName("Single even number: 0 is even")
    void testZeroIsEven() {
        BfhlRequest req = new BfhlRequest(List.of("0"));
        BfhlResponse res = bfhlService.process(req);

        assertThat(res.getEvenNumbers()).containsExactly("0");
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getSum()).isEqualTo("0");
    }

    @Test
    @DisplayName("Alphabet tokens are uppercased in output")
    void testAlphabetsUppercased() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("hello", "World"));
        BfhlResponse res = bfhlService.process(req);

        assertThat(res.getAlphabets()).containsExactly("HELLO", "WORLD");
    }

    @Test
    @DisplayName("Dash '-' is treated as a special character, not a number sign")
    void testDashIsSpecial() {
        BfhlRequest req = new BfhlRequest(List.of("-"));
        BfhlResponse res = bfhlService.process(req);

        assertThat(res.getSpecialCharacters()).containsExactly("-");
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getEvenNumbers()).isEmpty();
    }

    // =========================================================================
    // Helper-method unit tests (package-private methods)
    // =========================================================================

    @Test
    @DisplayName("isNumeric: digits-only strings are numeric")
    void testIsNumeric() {
        assertThat(serviceImpl.isNumeric("0")).isTrue();
        assertThat(serviceImpl.isNumeric("123")).isTrue();
        assertThat(serviceImpl.isNumeric("-1")).isFalse();   // negative sign → special
        assertThat(serviceImpl.isNumeric("1a")).isFalse();
        assertThat(serviceImpl.isNumeric("")).isFalse();
        assertThat(serviceImpl.isNumeric(null)).isFalse();
    }

    @Test
    @DisplayName("isAlphabetic: letter-only strings are alphabetic")
    void testIsAlphabetic() {
        assertThat(serviceImpl.isAlphabetic("a")).isTrue();
        assertThat(serviceImpl.isAlphabetic("ABCD")).isTrue();
        assertThat(serviceImpl.isAlphabetic("a1")).isFalse();
        assertThat(serviceImpl.isAlphabetic("")).isFalse();
        assertThat(serviceImpl.isAlphabetic(null)).isFalse();
    }

    @Test
    @DisplayName("buildConcatString: empty list returns empty string")
    void testConcatEmpty() {
        assertThat(serviceImpl.buildConcatString(Collections.emptyList())).isEmpty();
    }

    @Test
    @DisplayName("buildConcatString: single char → uppercase")
    void testConcatSingle() {
        assertThat(serviceImpl.buildConcatString(List.of('a'))).isEqualTo("A");
    }

    @Test
    @DisplayName("buildConcatString: two chars → first upper, second lower (reversed)")
    void testConcatTwo() {
        // chars: [a, R]  → reversed: [R, a] → R(upper) a(lower) = "Ra"
        assertThat(serviceImpl.buildConcatString(Arrays.asList('a', 'R'))).isEqualTo("Ra");
    }
}
