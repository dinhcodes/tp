package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_PREFIX;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.RoomNumber;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static StudentId parseStudentId(String studentId) throws ParseException {
        requireNonNull(studentId);
        String trimmedStudentId = studentId.trim();
        if (!StudentId.isValidStudentId(trimmedStudentId)) {
            throw new ParseException(StudentId.MESSAGE_CONSTRAINTS);
        }
        return new StudentId(trimmedStudentId);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String roomNumber} into a {@code RoomNumber}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code roomNumber} is invalid.
     */
    public static RoomNumber parseRoomNumber(String roomNumber) throws ParseException {
        requireNonNull(roomNumber);
        String trimmedRoomNumber = roomNumber.trim();
        if (!RoomNumber.isValidRoomNumber(trimmedRoomNumber)) {
            throw new ParseException(RoomNumber.MESSAGE_CONSTRAINTS);
        }
        return new RoomNumber(trimmedRoomNumber);
    }

    /**
     * Parses a {@code String emergencyContact} into an {@code EmergencyContact}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code emergencyContact} is invalid.
     */
    public static EmergencyContact parseEmergencyContact(String emergencyContact) throws ParseException {
        requireNonNull(emergencyContact);
        String trimmedEmergencyContact = emergencyContact.trim();
        if (!EmergencyContact.isValidEmergencyContact(trimmedEmergencyContact)) {
            throw new ParseException(EmergencyContact.MESSAGE_CONSTRAINTS);
        }
        return new EmergencyContact(trimmedEmergencyContact);
    }

    /**
     * Parses a {@code String year} into a normalized year tag value. Accepts formats like {@code 1}, {@code Y1},
     * {@code year 1} and returns {@code 1}. If the input is empty, it returns null.
     *
     * @param year the input year string to parse
     * @return the parsed year string, or null if the input is empty
     * @throws ParseException if the given {@code year} is invalid.
     */
    public static String parseYear(String year) throws ParseException {
        requireNonNull(year);
        String trimmedYear = year.trim().toUpperCase();

        String normalizedYear = null;
        if (trimmedYear.startsWith("Y")) {
            normalizedYear = trimmedYear.substring(1).trim();
        }
        if (trimmedYear.startsWith("Year")) {
            normalizedYear = trimmedYear.substring(4).trim();
        }
        if (trimmedYear.isEmpty()) {
            normalizedYear = null; // Allow empty input to be treated as null
        }

        if (!TagType.YEAR.isValidTagName(normalizedYear)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }

        return normalizedYear;
    }

    /**
     * Parses a {@code String gender} into a normalized gender tag value. Accepts formats like {@code he}, {@code him},
     * {@code he/him} and returns {@code he/him}. If the input is empty, it returns null.
     *
     * @param gender the input gender
     * @return the parsed gender, or null if the input is empty
     * @throws ParseException if the given {@code gender} is invalid.
     */
    public static String parseGender(String gender) throws ParseException {
        requireNonNull(gender);
        String trimmedGender = gender.trim().toLowerCase();

        return switch (trimmedGender) {
            case "he", "him", "he/him" -> "he/him";
            case "she", "her", "she/her" -> "she/her";
            case "they", "them", "they/them" -> "they/them";
            case "" -> null; // Allow empty input to be treated as null
            default -> throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        };
    }

    /**
     * Parses a {@code String input} into a positive integer.
     *
     * @throws ParseException if the given {@code input} is not a positive integer.
     */
    public static int parseDemeritIndex(String input) throws ParseException {
        requireNonNull(input);
        String trimmedInput = input.trim();
        try {
            int value = Integer.parseInt(trimmedInput);
            if (value <= 0) {
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException e) {
            throw new ParseException("Demerit rule index must be a positive integer.");
        }
    }

    /**
     * Checks if the given {@code args} contains any occurances of "%=" that are not in the list of
     * {@code knownPrefixes}. If there are, a ParseException is thrown with a message indicating the unknown prefix
     * and the correct {@code usage format}.
     *
     * @param args the input arguments to check for unknown prefixes
     * @param usageFormat the correct usage format to include in the exception message if an unknown prefix is found
     * @param knownPrefixes the list of known prefixes to check against
     * @throws ParseException if an unknown prefix is found in the input arguments
     */
    public static void checkForUnknownPrefixes(String args, String usageFormat, Prefix... knownPrefixes)
            throws ParseException {
        String unknownPrefix = ArgumentTokenizer.checkForUnknownPrefixes(args, knownPrefixes);

        if (!unknownPrefix.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_UNKNOWN_PREFIX, unknownPrefix)
                    + "\n" + usageFormat);
        }
    }
}

