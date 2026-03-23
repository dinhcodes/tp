package seedu.address.logic.commands;

import seedu.address.model.Model;
import seedu.address.model.demerit.DemeritRuleCatalogue;

/**
 * Lists all available demerit rules and their point tiers.
 */
public class DemeritListCommand extends Command {

    public static final String COMMAND_WORD = "demeritlist";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists the indexed demerit rules and their point tiers.\n"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(DemeritRuleCatalogue.formatAllRules());
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof DemeritListCommand;
    }
}