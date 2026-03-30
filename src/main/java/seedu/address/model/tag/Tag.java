package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in
 */
public class Tag {

    public static final String MESSAGE_CONSTRAINTS = "Tags names should be alphanumeric. "
            + "\nGender tags can be: she/her, he/him or they/them"
            + "\nYear tags should be a positive integer between 1 and 6 inclusive.";

    public final String tagName;
    public final TagType tagType;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagType The type of the tag.
     * @param tagName A valid tag name.
     */
    public Tag(TagType tagType, String tagName) {
        requireNonNull(tagName);
        requireNonNull(tagName);

        checkArgument(isValidTagName(tagName, tagType), MESSAGE_CONSTRAINTS);

        this.tagName = getNormalisedTagName(tagName, tagType);
        this.tagType = tagType;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test, TagType type) {
        requireNonNull(type);
        return type.isValidTagName(getNormalisedTagName(test, type));
    }

    public static String getNormalisedTagName(String test, TagType type) {
        return type == TagType.GENDER ? test.trim().toLowerCase() : test;
    }

    /**
     * @return the tag name of this tag.
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * @return the tag type of this tag.
     */
    public TagType getTagType() {
        return tagType;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Tag otherTag)) {
            return false;
        }
        return tagType == otherTag.tagType && tagName.equals(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
