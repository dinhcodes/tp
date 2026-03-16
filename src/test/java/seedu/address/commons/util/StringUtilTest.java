package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringUtilTest {

    //---------------- Tests for isNonZeroUnsignedInteger --------------------------------------

    @Test
    public void isNonZeroUnsignedInteger() {

        // EP: empty strings
        assertFalse(StringUtil.isNonZeroUnsignedInteger("")); // Boundary value
        assertFalse(StringUtil.isNonZeroUnsignedInteger("  "));

        // EP: not a number
        assertFalse(StringUtil.isNonZeroUnsignedInteger("a"));
        assertFalse(StringUtil.isNonZeroUnsignedInteger("aaa"));

        // EP: zero
        assertFalse(StringUtil.isNonZeroUnsignedInteger("0"));

        // EP: zero as prefix
        assertTrue(StringUtil.isNonZeroUnsignedInteger("01"));

        // EP: signed numbers
        assertFalse(StringUtil.isNonZeroUnsignedInteger("-1"));
        assertFalse(StringUtil.isNonZeroUnsignedInteger("+1"));

        // EP: numbers with white space
        assertFalse(StringUtil.isNonZeroUnsignedInteger(" 10 ")); // Leading/trailing spaces
        assertFalse(StringUtil.isNonZeroUnsignedInteger("1 0")); // Spaces in the middle

        // EP: number larger than Integer.MAX_VALUE
        assertFalse(StringUtil.isNonZeroUnsignedInteger(Long.toString(Integer.MAX_VALUE + 1)));

        // EP: valid numbers, should return true
        assertTrue(StringUtil.isNonZeroUnsignedInteger("1")); // Boundary value
        assertTrue(StringUtil.isNonZeroUnsignedInteger("10"));
    }

    //---------------- Tests for matchesWordInSetIgnoreCase --------------------------------------

    @Test
    public void matchesWordInSetIgnoreCase_nullOrEmptyInputs_throwsException() {
        // Null inputs
        Assertions.assertThrows(NullPointerException.class, () ->
                StringUtil.matchesWordInSetIgnoreCase(null, Set.of("abc")));
        Assertions.assertThrows(NullPointerException.class, () ->
                StringUtil.matchesWordInSetIgnoreCase("abc", null));

        // Empty inputs
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                StringUtil.matchesWordInSetIgnoreCase("", Set.of("abc")));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                StringUtil.matchesWordInSetIgnoreCase("abc", Collections.emptySet()));
    }

    @Test
    public void matchesWordInSetIgnoreCase_validInputs_correctResult() {
        Set<String> wordSet = Set.of("Alice", "Bob", "Charlie");

        // Exact match
        assertTrue(StringUtil.matchesWordInSetIgnoreCase("Alice", wordSet));

        // Case-insensitive match
        assertTrue(StringUtil.matchesWordInSetIgnoreCase("alice", wordSet));
        assertTrue(StringUtil.matchesWordInSetIgnoreCase("BOB", wordSet));

        // Whitespace trimmed match
        assertTrue(StringUtil.matchesWordInSetIgnoreCase("  Charlie  ", wordSet));

        // No match
        assertFalse(StringUtil.matchesWordInSetIgnoreCase("Dave", wordSet));
        assertFalse(StringUtil.matchesWordInSetIgnoreCase("Ali", wordSet)); // Partial match is false for this method
    }

    //---------------- Tests for matchesSubstringInSetIgnoreCase --------------------------------------

    @Test
    public void matchesSubstringInSetIgnoreCase_nullOrEmptyInputs_throwsException() {
        Assertions.assertThrows(NullPointerException.class, () ->
                StringUtil.matchesSubstringInSetIgnoreCase(null, Set.of("abc")));
        Assertions.assertThrows(NullPointerException.class, () ->
                StringUtil.matchesSubstringInSetIgnoreCase("abc", null));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                StringUtil.matchesSubstringInSetIgnoreCase("", Set.of("abc")));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                StringUtil.matchesSubstringInSetIgnoreCase("abc", Collections.emptySet()));
    }

    @Test
    public void matchesSubstringInSetIgnoreCase_validInputs_correctResult() {
        // Nota: Según la implementación, verifica si 'word' CONTIENE algún elemento del 'set'.
        Set<String> keywords = Set.of("Ali", "Bo");

        // Substring match: "Alice" contiene "Ali"
        assertTrue(StringUtil.matchesSubstringInSetIgnoreCase("Alice", keywords));

        // Case-insensitive substring
        assertTrue(StringUtil.matchesSubstringInSetIgnoreCase("alice", keywords));
        assertTrue(StringUtil.matchesSubstringInSetIgnoreCase("BOB", keywords));

        // Keyword más larga que la palabra (No match)
        assertFalse(StringUtil.matchesSubstringInSetIgnoreCase("Al", keywords));

        // No match
        assertFalse(StringUtil.matchesSubstringInSetIgnoreCase("Charlie", keywords));
    }

    //---------------- Tests for fuzzyMatchesIgnoresCase --------------------------------------

    @Test
    public void fuzzyMatchesIgnoresCase_nullOrEmptyInputs_throwsException() {
        Assertions.assertThrows(NullPointerException.class, () -> StringUtil.fuzzyMatchesIgnoresCase(null, "abc"));
        Assertions.assertThrows(NullPointerException.class, () -> StringUtil.fuzzyMatchesIgnoresCase("abc", null));

        Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtil.fuzzyMatchesIgnoresCase("", "abc"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtil.fuzzyMatchesIgnoresCase("abc", ""));
    }

    @Test
    public void fuzzyMatchesIgnoresCase_exactMatches_returnsTrue() {
        assertTrue(StringUtil.fuzzyMatchesIgnoresCase("abc", "abc"));
        assertTrue(StringUtil.fuzzyMatchesIgnoresCase("abc", "ABC")); // Case-insensitive
        // Short strings exact match
        assertTrue(StringUtil.fuzzyMatchesIgnoresCase("a", "a"));
        assertTrue(StringUtil.fuzzyMatchesIgnoresCase("ab", "ab"));
    }

    @Test
    public void fuzzyMatchesIgnoresCase_shortStringsWithDifferences_returnsFalse() {
        // Strings <= 2 chars logic: returns false if not exact match (according to impl)
        assertFalse(StringUtil.fuzzyMatchesIgnoresCase("ab", "ac"));
        assertFalse(StringUtil.fuzzyMatchesIgnoresCase("a", "b"));
    }

    @Test
    public void fuzzyMatchesIgnoresCase_withinDistance_returnsTrue() {
        // Distance 1
        assertTrue(StringUtil.fuzzyMatchesIgnoresCase("kitten", "sitten"));
        assertTrue(StringUtil.fuzzyMatchesIgnoresCase("kitten", "kittn"));

        // Distance 2
        assertTrue(StringUtil.fuzzyMatchesIgnoresCase("kitten", "kitti"));
        assertTrue(StringUtil.fuzzyMatchesIgnoresCase("example", "exmpl"));
    }

    @Test
    public void fuzzyMatchesIgnoresCase_exceedDistance_returnsFalse() {
        // Distance > 2
        assertFalse(StringUtil.fuzzyMatchesIgnoresCase("kitten", "kitchren"));
        assertFalse(StringUtil.fuzzyMatchesIgnoresCase("hello", "olleh"));
    }

    //---------------- Tests for fuzzyMatchesWordInSetIgnoreCase --------------------------------------

    @Test
    public void fuzzyMatchesWordInSetIgnoreCase_validInputs_correctResult() {
        Set<String> wordSet = Set.of("kitten", "puppy");

        // Exact match
        assertTrue(StringUtil.fuzzyMatchesWordInSetIgnoreCase("kitten", wordSet));

        // Fuzzy match (1 edit)
        assertTrue(StringUtil.fuzzyMatchesWordInSetIgnoreCase("sitten", wordSet));

        // Fuzzy match (2 edits)
        assertTrue(StringUtil.fuzzyMatchesWordInSetIgnoreCase("kitti", wordSet));

        // No match (too different)
        assertFalse(StringUtil.fuzzyMatchesWordInSetIgnoreCase("dragon", wordSet));
    }

    //---------------- Tests for getDetails --------------------------------------

    /*
     * Equivalence Partitions: null, valid throwable object
     */

    @Test
    public void getDetails_exceptionGiven() {
        assertTrue(StringUtil.getDetails(new FileNotFoundException("file not found"))
            .contains("java.io.FileNotFoundException: file not found"));
    }

    @Test
    public void getDetails_nullGiven_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.getDetails(null));
    }
}
