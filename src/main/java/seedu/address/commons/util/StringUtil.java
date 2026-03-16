package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.text.similarity.LevenshteinDistance;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {

    /**
     * Returns true if any of the words in the {@code wordSet} exactly matches the {@code word}.
     * <br>
     * Ignores case, a full word match is required.
     * @param word word to be checked against the wordSet, cannot be null or empty
     * @param wordSet set of words to be checked against the word, cannot be null or empty
     * @return true if any of the words in the wordSet is an exact match, false otherwise
     */
    public static boolean matchesWordInSetIgnoreCase(String word, Set<String> wordSet) {
        requireNonNull(word);
        requireNonNull(wordSet);

        String preppedWord = word.toLowerCase().trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(!wordSet.isEmpty(), "Word set cannot be empty");

        return wordSet.stream()
                .map(k -> k.toLowerCase().trim())
                .anyMatch(preppedWord::equals);
    }

    /**
     * Returns true if any of the words in the {@code wordSet} contains the {@code word}.
     * <br>
     * Ignores case, only a substring match is required.
     * @param word word to be checked against the wordSet, cannot be null or empty
     * @param wordSet set of words to be checked against the word, cannot be null or empty
     * @return true if any of the words in the wordSet contains word as a substring, false otherwise
     */
    public static boolean matchesSubstringInSetIgnoreCase(String word, Set<String> wordSet) {
        requireNonNull(word);
        requireNonNull(wordSet);

        String preppedWord = word.toLowerCase().trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(!wordSet.isEmpty(), "Word set cannot be empty");

        return wordSet.stream()
                .map(k -> k.toLowerCase().trim())
                .anyMatch(preppedWord::contains);
    }

    /**
     * Returns true if any of the words in the {@code wordSet} fuzzy matches the {@code word}.
     * <br>
     * Ignores case, fuzzy match is done through the Levenshtein distance
     * algorithm in {@link #fuzzyMatchesIgnoresCase(String, String)}.
     */
    public static boolean fuzzyMatchesWordInSetIgnoreCase(String word, Set<String> wordSet) {
        requireNonNull(word);
        requireNonNull(wordSet);

        String preppedWord = word.toLowerCase().trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(!wordSet.isEmpty(), "Word set cannot be empty");

        return wordSet.stream()
                .map(k -> k.toLowerCase().trim())
                .anyMatch(k -> fuzzyMatchesIgnoresCase(preppedWord, k));
    }

    /**
     * Returns {@code true} if the two strings are similar enough to be considered a match.
     * <br>
     * Exact matches are always returned as true.
     * For strings longer than 2 characters, a Levenshtein distance of up to 2 edits is allowed for small typos.
     * Read more about Levenshtein distance <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">here</a>
     * <br>
     * examples:<pre>
     *    fuzzyMatchIgnoresCase("ABc", "abc") == true
     *    fuzzyMatchIgnoresCase("abc", "acd") == true // delete c, add d
     *    fuzzyMatchIgnoresCase("abc", "ccc") == false // requires 3 edits
     *    </pre>
     * @param s1 String to compare
     * @param s2 String to compare
     * @return true if the strings match exactly or fall within the typo threshold
     */
    public static boolean fuzzyMatchesIgnoresCase(String s1, String s2) {
        requireNonNull(s1);
        requireNonNull(s2);

        checkArgument(!s1.isEmpty(), "Word parameter cannot be empty");
        checkArgument(!s2.isEmpty(), "Word parameter cannot be empty");

        String s1Processed = s1.toLowerCase().trim();
        String s2Processed = s2.toLowerCase().trim();

        if (s1Processed.equals(s2Processed)) {
            return true;
        }

        if (s1Processed.length() <= 2 || s2Processed.length() <= 2) {
            return false;
        }

        LevenshteinDistance levenshtein = new LevenshteinDistance(2);
        Integer distance = levenshtein.apply(s1Processed, s2Processed);

        return distance != null && distance != -1;
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer
     * e.g. 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-1", "0", "+1", and " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            return value > 0 && !s.startsWith("+"); // "+1" is successfully parsed by Integer#parseInt(String)
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Splits a given sentence into words and returns a set of the words in the sentence.
     *
     * @param sentence sentence to be split into words. Cannot be null.
     * @return a {@code Set<String>} of the words in the sentence.
     */
    public static Set<String> splitSentenceIntoWords(String sentence) {
        requireNonNull(sentence);
        String trimmedSentence = sentence.trim();
        if (trimmedSentence.isEmpty()) {
            return new HashSet<>();
        }
        String[] wordsInSentence = trimmedSentence.split("\\s+");
        return new HashSet<>(Arrays.asList(wordsInSentence));
    }
}
