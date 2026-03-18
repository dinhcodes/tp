package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROOM_NUMBER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args,
                        PREFIX_NAME,
                        PREFIX_PHONE,
                        PREFIX_EMAIL,
                        PREFIX_STUDENT_ID,
                        PREFIX_ROOM_NUMBER,
                        PREFIX_EMERGENCY_CONTACT,
                        PREFIX_TAG);

        String preamble = argMultimap.getPreamble().trim();

        StudentId targetStudentId;

        try {
            targetStudentId = ParserUtil.parseStudentId(preamble);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_STUDENT_ID,
                PREFIX_ROOM_NUMBER, PREFIX_EMERGENCY_CONTACT);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_STUDENT_ID).isPresent()) {
            editPersonDescriptor.setStudentId(ParserUtil.parseStudentId(argMultimap.getValue(PREFIX_STUDENT_ID).get()));
        }
        if (argMultimap.getValue(PREFIX_ROOM_NUMBER).isPresent()) {
            editPersonDescriptor.setRoomNumber(ParserUtil.parseRoomNumber(argMultimap
                    .getValue(PREFIX_ROOM_NUMBER).get()));
        }
        if (argMultimap.getValue(PREFIX_EMERGENCY_CONTACT).isPresent()) {
            editPersonDescriptor.setEmergencyContact(ParserUtil.parseEmergencyContact(argMultimap
                    .getValue(PREFIX_EMERGENCY_CONTACT).get()));
        }

        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG))
                .ifPresent(editPersonDescriptor::setTags);


        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(targetStudentId, editPersonDescriptor);
    }

    /**
     * Parses {@code List<String> tags} into a {@code HashMap<TagType, Tag>} if non-empty.
     * Each tag string must follow the format "TYPE:VALUE" e.g. "MAJOR:CS", "YEAR:Y1".
     * If a single empty string is given (i.e. "t/" with no value), clears all tags.
     */
    private Optional<HashMap<TagType, Tag>> parseTagsForEdit(List<String> tags) throws ParseException {
        requireNonNull(tags);

        if (tags.isEmpty()) {
            return Optional.empty(); // no t/ prefix given at all — don't touch tags
        }

        if (tags.size() == 1 && tags.contains("")) {
            return Optional.of(new HashMap<>()); // t/ with no value = clear all tags
        }

        HashMap<TagType, Tag> tagMap = new HashMap<>();
        for (String tagEntry : tags) {
            String[] parts = tagEntry.split(":", 2);
            if (parts.length != 2) {
                throw new ParseException(
                        "Tag format should be TYPE:VALUE e.g. MAJOR:CS, YEAR:Y1, GENDER:M");
            }
            TagType type;
            try {
                type = TagType.valueOf(parts[0].trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ParseException("Invalid tag type: " + parts[0].trim()
                        + ". Valid types are: YEAR, MAJOR, GENDER");
            }
            String tagName = parts[1].trim();
            tagMap.put(type, new Tag(type, tagName));
        }
        return Optional.of(tagMap);
    }
}
