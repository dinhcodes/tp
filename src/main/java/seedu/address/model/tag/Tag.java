package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Tag in hall ledger.
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
     * @param tagContent valid tag content.
     */
    public Tag(TagType tagType, String tagContent) {
        requireNonNull(tagType);
        requireNonNull(tagContent);

        checkArgument(isValidTagName(tagContent, tagType), MESSAGE_CONSTRAINTS);

        this.tagName = getNormalisedTagName(tagContent, tagType);
        this.tagType = tagType;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test, TagType type) {
        requireNonNull(type);
        return type.isValidTagContent(getNormalisedTagName(test, type));
    }

    public static String getNormalisedTagName(String test, TagType type) {
        // check for gender as it is the only case-insensitive tag type
        return type == TagType.GENDER ? test.toLowerCase() : test;
    }

    public String getTagName() {
        return tagName;
    }

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

    @Override
    public String toString() {
        return '[' + tagName + ']';
    }

}
