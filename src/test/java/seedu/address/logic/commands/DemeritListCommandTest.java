package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.demerit.DemeritRuleCatalogue;

/**
 * Contains unit tests for {@code DemeritListCommand}.
 */
public class DemeritListCommandTest {

    private final Model model = new ModelManager();

    @Test
    public void execute_success() {
        DemeritListCommand demeritListCommand = new DemeritListCommand();

        CommandResult commandResult = demeritListCommand.execute(model);

        assertEquals(DemeritRuleCatalogue.formatAllRules(), commandResult.getFeedbackToUser());
    }
}
