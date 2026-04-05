package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.util.ModelUtil.getPersonByStudentIdOrThrow;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.model.person.StudentId;

/**
 * Adds a remark to a resident identified using unique StudentId.
 */
public class RemarkCommand extends Command {

    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a remark to the selected person.\n"
            + "Parameters: i=STUDENT_ID rm=REMARK\n"
            + "Example: " + COMMAND_WORD
            + " i=A1234567Z rm=Is vegetarian";

    public static final String MESSAGE_SUCCESS = "Updated Remark for Resident: %1$s";

    private final StudentId targetStudentId;
    private final Remark remark;

    /**
     * Creates a {@code RemarkCommand} to add the specified {@code Remark}
     * to the resident identified by the given {@code StudentId}.
     */
    public RemarkCommand(StudentId studentId, Remark remark) {
        requireNonNull(studentId);
        requireNonNull(remark);

        this.targetStudentId = studentId;
        this.remark = remark;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToRemark = getPersonByStudentIdOrThrow(model, targetStudentId);

        Person editedPerson = createEditedPerson(personToRemark, remark);

        model.setPerson(personToRemark, editedPerson);
        model.showAllPersons();
        model.setSelectedPerson(editedPerson); // Select the edited person in the UI

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a new {@code Person} with the specified remark.
     * All other fields remain unchanged.
     *
     * @param personToRemark the original resident whose details are to be copied.
     * @param remark the new remark to assign to the resident.
     * @return a new {@code Person} instance with the updated remark.
     */
    private static Person createEditedPerson(Person personToRemark, Remark remark) {
        Person newPerson = new Person(
                personToRemark.getName(),
                personToRemark.getPhone(),
                personToRemark.getEmail(),
                personToRemark.getStudentId(),
                personToRemark.getRoomNumber(),
                personToRemark.getEmergencyContact(),
                remark, // add the new remark or overwrite the existing remark
                personToRemark.getTags(),
                personToRemark.getDemeritIncidents()
        );
        assert newPerson.getRemark().equals(remark) : "New person remark should be the same as the input remark";
        return newPerson;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RemarkCommand otherRemarkCommand)) {
            return false;
        }

        return targetStudentId.equals(otherRemarkCommand.targetStudentId)
                && remark.equals(otherRemarkCommand.remark);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("studentId", targetStudentId)
                .add("remark", remark)
                .toString();
    }
}
