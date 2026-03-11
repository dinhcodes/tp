package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_EMPTY_ARGUMENT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROOM_NUMBER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_MAJOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_YEAR;

import java.util.Arrays;
import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentTokenizer tokenizer = new ArgumentTokenizer();
        ArgumentMultimap argMultimap = tokenizer.tokenize(args,
                PREFIX_NAME, PREFIX_EMAIL, PREFIX_PHONE, PREFIX_ROOM_NUMBER, PREFIX_STUDENT_ID,
                PREFIX_EMERGENCY_CONTACT, PREFIX_TAG, PREFIX_TAG_YEAR, PREFIX_TAG_MAJOR, PREFIX_TAG_GENDER)
                .removeEmptyValues();

        // Preamble and prefixes are both empty -> Empty argument message
        if (argMultimap.getPreamble().isEmpty() && argMultimap.hasEmptyPrefixArguments()) {
            throw new ParseException(String.format(MESSAGE_EMPTY_ARGUMENT, FindCommand.MESSAGE_USAGE));
        }

        // Both preamble and prefixes exists -> Invalid command format message
        if (!argMultimap.getPreamble().isEmpty() && !argMultimap.hasEmptyPrefixArguments()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // Preamble exists, prefixes are empty -> Search by name
        if (!argMultimap.getPreamble().isEmpty() && argMultimap.hasEmptyPrefixArguments()) {
            List<String> nameKeywords = getNameKeywords(args);
            return new FindCommand(new NameContainsKeywordsPredicate(nameKeywords));
        }

        // Preamble is empty, prefixes exist -> Search by prefixes
        // TODO: Implement search by prefixes in FindCommand
        if (argMultimap.getPreamble().isEmpty() && !argMultimap.hasEmptyPrefixArguments()) {
            return new FindCommand(new NameContainsKeywordsPredicate(List.of()));
        }
        return null;
    }


    /**
     * Extract name keywords from the preamble. Name keywords are separated by whitespaces.
     *
     * @param args the preamble
     * @return a list of name keywords
     * @throws ParseException if the preamble is empty or contains only whitespaces
     */
    private static List<String> getNameKeywords(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return Arrays.stream(trimmedArgs.split("\\s+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
