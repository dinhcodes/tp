package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;


import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.TagCommand.MESSAGE_USAGE;

public class TagCommandParser implements Parser<TagCommand> {


    public TagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argumentMultimap =
                ArgumentTokenizer.tokenize(args,
                        CliSyntax.PREFIX_TAG_GENDER,
                        CliSyntax.PREFIX_TAG_MAJOR,
                        CliSyntax.PREFIX_TAG_YEAR);

        Index index;

        try {
            index = ParserUtil.parseIndex(argumentMultimap.getPreamble());
        } catch(ParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE), e);
        }

        Set<Tag> tags = new HashSet<>();

        argumentMultimap.getValue(CliSyntax.PREFIX_TAG_GENDER)
                .ifPresent(gender -> tags.add(new Tag(gender)));

        argumentMultimap.getValue(CliSyntax.PREFIX_TAG_MAJOR)
                .ifPresent(major -> tags.add(new Tag(major)));

        argumentMultimap.getValue(CliSyntax.PREFIX_TAG_YEAR)
                .ifPresent(year -> tags.add(new Tag(year)));

        if (tags.isEmpty()) {
            throw new ParseException(TagCommand.TAG_NOT_ADDED);
        }

        return new TagCommand(index, tags);
    }
}
