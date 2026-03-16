package seedu.address.model.person;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    /**
     * Creates a {@code NameContainsKeywordsPredicate} with the given list of keywords.
     * <br>
     * @param keywords the list of keywords to be used for matching the person's name
     */
    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * Returns true if the person's name fuzzy matches any of the keywords.
     * Fuzzy matching is done by {@link StringUtil#fuzzyMatchesIgnoresCase(String, String)}.
     * <br>
     * @param person the person to be tested against the keywords
     * @return true if the person's name fuzzy matches any of the keywords, false otherwise
     */
    @Override
    public boolean test(Person person) {
        Set<String> keywordsSet = Set.copyOf(keywords);
        return StringUtil.fuzzyMatchesAnyIgnoreCase(person.getName().fullName, keywordsSet);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate)) {
            return false;
        }

        NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (NameContainsKeywordsPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override

    public String toString() {
        return "Name keywords: " + keywords.toString();
    }
}
