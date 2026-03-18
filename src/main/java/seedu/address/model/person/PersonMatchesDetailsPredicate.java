package seedu.address.model.person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.model.FilterDetails;
import seedu.address.model.tag.Tag;

/**
 * Tests whether a {@code Person} matches the details specified in a {@link FilterDetails}.
 */
public class PersonMatchesDetailsPredicate implements Predicate<Person> {

    private final FilterDetails filterDetails;

    /**
     * Creates a {@code PersonMatchesDetailsPredicate} with the given {@code FilterDetails}.
     * <br>
     * @param filterDetails person details to be used for matching.
     */
    public PersonMatchesDetailsPredicate(FilterDetails filterDetails) {
        this.filterDetails = Objects.requireNonNull(filterDetails);
    }

    @Override
    public boolean test(Person person) {
        return isNameMatch(person)
                & isFuzzyMatch(person.getEmail().value, filterDetails.getEmailKeywords())
                & isFuzzyMatch(person.getPhone().value, filterDetails.getPhoneNumberKeywords())
                & isExactMatch(person.getRoomNumber().value, filterDetails.getRoomNumberKeywords())
                & isFuzzyMatch(person.getStudentId().value, filterDetails.getStudentIdKeywords())
                & isExactMatch(person.getEmergencyContact().value, filterDetails.getEmergencyContactKeywords())
                & isExactMatchTags(new HashSet<>(person.getTags().values()), filterDetails.getTagYearKeywords())
                & isExactMatchTags(new HashSet<>(person.getTags().values()), filterDetails.getTagMajorKeywords())
                & isExactMatchTags(new HashSet<>(person.getTags().values()), filterDetails.getTagGenderKeywords());
    }

    private boolean isNameMatch(Person person) {
        if (filterDetails.getNameKeywords().isEmpty()) {
            return true;
        }
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(new ArrayList<>(filterDetails.getNameKeywords()));
        return predicate.test(person);
    }

    private boolean isExactMatch(String fieldValue, Set<String> keywords) {
        assert keywords != null : "keywords set should be non-null";
        if (keywords.isEmpty()) {
            return true;
        }
        if (fieldValue.isEmpty()) {
            return false;
        }
        String lower = fieldValue.toLowerCase(Locale.ROOT);
        return keywords.stream().map(k -> k.toLowerCase(Locale.ROOT)).anyMatch(lower::equals);
    }

    private boolean isFuzzyMatch(String fieldValue, Set<String> keywords) {
        assert keywords != null : "keywords set should be non-null";
        if (keywords.isEmpty()) {
            return true;
        }
        if (fieldValue.isEmpty()) {
            return false;
        }
        String lower = fieldValue.toLowerCase(Locale.ROOT);
        return keywords.stream().map(k -> k.toLowerCase(Locale.ROOT)).anyMatch(lower::contains);
    }

    /**
     * Checks if any of the {@code personTags} match any of the {@code keywords}.
     * Fuzzy matching allows for minor typos or differences.
     * Substring matching checks if the keyword is contained within the value.
     */
    private boolean isFuzzyMatchTags(Set<Tag> personTags, Set<String> keywords) {
        assert keywords != null : "tag keyword set should be non-null";
        if (keywords.isEmpty()) {
            return true;
        }
        return personTags.stream().anyMatch(tag -> {
            String lowerTag = tag.tagName.toLowerCase(Locale.ROOT);
            return keywords.stream()
                    .map(k -> k.toLowerCase(Locale.ROOT))
                    .anyMatch(lowerTag::contains);
        });
    }

    /**
     * Checks if any of the {@code personTags} exactly match any of the {@code keywords} (case-insensitive).
     *
     * @param personTags The set of tags from the person.
     * @param keywords The set of keywords to match against.
     * @return True if any tag exactly matches any keyword, false otherwise.
     */
    private boolean isExactMatchTags(Set<Tag> personTags, Set<String> keywords) {
        assert keywords != null : "tag keyword set should be non-null";
        if (keywords.isEmpty()) {
            return true;
        }
        return personTags.stream()
                .anyMatch(tag -> keywords.stream()
                        .anyMatch(keyword -> tag.tagName.equalsIgnoreCase(keyword)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PersonMatchesDetailsPredicate)) {
            return false;
        }
        PersonMatchesDetailsPredicate otherPredicate = (PersonMatchesDetailsPredicate) other;
        return Objects.equals(this.filterDetails, otherPredicate.filterDetails);
    }

    @Override
    public String toString() {
        return "Filter Details:"
                + filterDetails.toString();
    }
}
