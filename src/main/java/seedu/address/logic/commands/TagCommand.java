package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_MAJOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_YEAR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;

/**
 * Adds a tag to a resident in the hall ledger.
 * The resident is identified using the index number from the displayed resident list.
 * Existing tags will be overwritten by the newly provided tags.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":Adds a tag to the resident in the hall ledger"
            + "by the index number used in the displayed resident list."
            + "Existing tags will be overwritten by the input tags.\n"
            + "Parameters: i=STUDENT_ID (must be a valid student ID) "
            + "[" + PREFIX_TAG_YEAR + "YEAR] "
            + "[" + PREFIX_TAG_MAJOR + "MAJOR] "
            + "[" + PREFIX_TAG_GENDER + "GENDER] "
            + "Example: " + COMMAND_WORD + "1 "
            + PREFIX_TAG_YEAR + "2 "
            + PREFIX_TAG_MAJOR + "CS "
            + "Note if the student has a double major specify like "
            + "[" + PREFIX_TAG_MAJOR + "MAJOR1 and MAJOR2]";

    public static final String TAG_SUCCESS = "Added Tag to Resident: %1$s";
    public static final String TAG_NOT_ADDED = "At least one tag (year / major / gender) must be provided.";

    public final StudentId studentId;
    public final Map<TagType, Tag> tags;

    /**
     * @param studentId of the person in the filtered person list to edit
     * @param tags list of tags to add to the person
     */
    public TagCommand(StudentId studentId, Map<TagType, Tag> tags) {
        requireNonNull(studentId);
        requireNonNull(tags);
        this.studentId = studentId;
        this.tags = tags;
    }

    /**
     * Executes the tag command.
     *
     * @param model {@code Model} which the command should operate on.
     * @return a {@code CommandResult} describing the result of the command execution.
     */
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        Person personToTag = null;
        for (Person person : lastShownList) {
            if (person.getStudentId().equals(studentId)) {
                personToTag = person;
                break;
            }
        }

        if (personToTag == null) {
            throw new CommandException(String.format("ResidentNotFound: No resident "
                    + "found with student ID %s.", studentId));
        }

        HashMap<TagType, Tag> updatedTags = new HashMap<>(personToTag.getTags());
        updatedTags.putAll(tags);
        //checkTagLimits(updatedTags);

        Person taggedPerson = new Person(
                personToTag.getName(),
                personToTag.getPhone(),
                personToTag.getEmail(),
                personToTag.getStudentId(),
                personToTag.getRoomNumber(),
                personToTag.getEmergencyContact(),
                updatedTags
        );

        model.setPerson(personToTag, taggedPerson);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(TAG_SUCCESS, Messages.format(taggedPerson)));
    }
}
