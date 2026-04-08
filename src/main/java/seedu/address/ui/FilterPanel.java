package seedu.address.ui;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.FilterDetails;
import seedu.address.model.ReadOnlyFilterDetails;
import seedu.address.model.tag.TagType;
import seedu.address.ui.executors.FilterExecutor;
import seedu.address.ui.filter.FilterPanelComboBox;
import seedu.address.ui.filter.FilterPanelField;
import seedu.address.ui.filter.KeywordSetter;

/**
 * Panel containing the list of filtering options.
 *
 * <p>This class does two main jobs:
 * <ul>
 *   <li>It creates the small reusable filter inputs (text fields / combo boxes).</li>
 *   <li>It keeps those inputs in sync with the current {@link ReadOnlyFilterDetails}.</li>
 * </ul>
 *
 * <p><b>What happens when the user edits a filter:</b>
 * <ol>
 *   <li>The user adds/removes a keyword in a filter input.</li>
 *   <li>The input calls back into this panel with the new keyword list.</li>
 *   <li>We build a new {@link FilterDetails} (a copy of the current one), update the relevant field,
 *       then ask {@link FilterExecutor} to apply the filter.</li>
 *   <li>If applying the filter fails (throws {@link CommandException}), we keep showing the last accepted keywords.</li>
 * </ol>
 *
 * <p><b>What happens when filters change elsewhere </b> If filters are updated by non-GUI
 * action(e.g. CLI find commands), we listen to the {@link ObservableSet}s and refresh the UI so it stays up-to-date.
 */
public class FilterPanel extends UiPart<Region> {
    private static final String FXML = "FilterPanel.fxml";
    private final ReadOnlyFilterDetails filterDetails;
    // The callback used to execute filtering when there are changes from the GUI (e.g. user edits keywords in a field)
    private final FilterExecutor filterExecutor;

    @FXML
    private StackPane nameFilterFieldPlaceholder;
    @FXML
    private StackPane phoneFilterFieldPlaceholder;
    @FXML
    private StackPane emailFilterFieldPlaceholder;
    @FXML
    private StackPane studentIdFilterFieldPlaceholder;
    @FXML
    private StackPane roomNumberFilterFieldPlaceholder;
    @FXML
    private StackPane majorFilterFieldPlaceholder;
    @FXML
    private StackPane emergencyContactFilterFieldPlaceholder;
    @FXML
    private StackPane yearFilterFieldPlaceholder;
    @FXML
    private StackPane genderFilterFieldPlaceholder;

    /**
     * Creates a {@code FilterPanel} with the given {@code ReadOnlyFilterDetails}.
     *
     * @param filterDetails source-of-truth keyword sets to display
     * @param filterExecutor callback used to apply filtering when the user edits keywords in the GUI
     */
    public FilterPanel(ReadOnlyFilterDetails filterDetails, FilterExecutor filterExecutor) {
        super(FXML);
        this.filterDetails = filterDetails;
        this.filterExecutor = filterExecutor;
        fillInnerParts();
    }

    /**
     * Fills inner placeholders with reusable field components.
     *
     * <p>This sets up each filter field and connects it to:
     * <ul>
     *   <li>an {@link ObservableSet} in {@link ReadOnlyFilterDetails} (so the UI can refresh when filters change), and</li>
     *   <li>a {@link KeywordSetter} (so we can call this function to write the edited keywords into a new
     *   {@link FilterDetails} snapshot).</li>
     * </ul>
     *
     * <p><b>Example:</b> For "Search by Name", we pass {@code FilterDetails::setNameKeywords} so that when the user
     * edits the name keywords, the filter input field knows which part of {@link FilterDetails} to update.
     */
    private void fillInnerParts() {
        bindTextField(nameFilterFieldPlaceholder, "Search by Name", "E.g: Alex",
                filterDetails.getNameKeywords(), FilterDetails::setNameKeywords);

        bindTextField(phoneFilterFieldPlaceholder, "Search by Phone", "E.g: +65 91234567",
                filterDetails.getPhoneNumberKeywords(), FilterDetails::setPhoneNumberKeywords);

        bindTextField(emailFilterFieldPlaceholder, "Search by Email", "E.g: alex@example.com",
                filterDetails.getEmailKeywords(), FilterDetails::setEmailKeywords);

        bindTextField(studentIdFilterFieldPlaceholder, "Search by Student ID", "E.g: A1234567X",
                filterDetails.getStudentIdKeywords(), FilterDetails::setStudentIdKeywords);

        bindTextField(roomNumberFilterFieldPlaceholder, "Search by Room Number", "E.g: 12A or 12",
                filterDetails.getRoomNumberKeywords(), FilterDetails::setRoomNumberKeywords);

        bindTextField(majorFilterFieldPlaceholder, "Search by Major", "E.g: Computer Science",
                filterDetails.getTagMajorKeywords(), FilterDetails::setTagMajorKeywords);

        bindTextField(emergencyContactFilterFieldPlaceholder, "Search by Emergency Contact", "E.g: +65 98765432",
                filterDetails.getEmergencyContactKeywords(), FilterDetails::setEmergencyContactKeywords);

        bindComboBoxField(yearFilterFieldPlaceholder, "Search by Year", "E.g: 1",
                TagType.YEAR.getAllowedValues().orElseThrow(),
                filterDetails.getTagYearKeywords(), FilterDetails::setTagYearKeywords);

        bindComboBoxField(genderFilterFieldPlaceholder, "Search by Gender", "E.g: he/him",
                TagType.GENDER.getAllowedValues().orElseThrow(),
                filterDetails.getTagGenderKeywords(), FilterDetails::setTagGenderKeywords);
    }

    /**
     * Creates and binds a keyword field to an observable keyword source.
     *
     * <p><b>When the user edits keywords:</b>
     * <ol>
     *   <li>The field calls the {@code keywordsSetter} callback with the edited keyword list.</li>
     *   <li>We try to apply those keywords and run filtering.</li>
     *   <li>If something goes wrong, we return the old keywords.</li>
     * </ol>
     *
     * <p><b>When keywords change externally:</b> We listen to {@code sourceKeywords} and re-render the field.
     *
     * @param placeholder    target UI container
     * @param title          section label
     * @param promptText     placeholder text
     * @param sourceKeywords observable keyword set from {@link ReadOnlyFilterDetails} for this field
     * @param keywordSetter  setter that writes updated keywords
     */
    private void bindTextField(StackPane placeholder,
                               String title,
                               String promptText,
                               ObservableSet<String> sourceKeywords,
                               KeywordSetter keywordSetter) {
        FilterPanelField field = new FilterPanelField(
                title,
                promptText,
                // Callback: the field updates, apply the change and execute filtering with the new keywords
                keywords -> applyKeywordsAndExecuteFilter(
                        keywordSetter,
                        sourceKeywords,
                        new LinkedHashSet<>(keywords)));

        // Initialize the field with the current keywords from the source
        field.setKeywords(List.copyOf(sourceKeywords));
        placeholder.getChildren().setAll(field.getRoot());

        // Listen for changes in the source keyword set and update the field UI accordingly
        sourceKeywords.addListener((SetChangeListener<? super String>) ignoredChange ->
                field.setKeywords(List.copyOf(sourceKeywords)));
    }

    /**
     * Binds a combo-box based filter field to the keywords it is supposed to display.
     *
     * <p>This is the combo-box version of {@link #bindTextField(StackPane, String, String, ObservableSet, KeywordSetter)}.
     * The idea is the same: the user picks values, we attempt to apply them, and the UI stays in sync with
     * {@code sourceKeywords}.
     *
     * @param placeholder target UI container to populate with the field
     * @param title section label
     * @param promptText placeholder text displayed in the input control
     * @param options allowed options shown in the combo-box dropdown
     * @param sourceKeywords observable keyword set from {@link ReadOnlyFilterDetails} for this field
     * @param keywordSetter setter for writing updated keywords into a {@link FilterDetails} snapshot
     */
    private void bindComboBoxField(StackPane placeholder,
                                   String title,
                                   String promptText,
                                   List<String> options,
                                   ObservableSet<String> sourceKeywords,
                                   KeywordSetter keywordSetter) {
        FilterPanelComboBox field = new FilterPanelComboBox(
                title,
                promptText,
                options,
                // Callback: When the field updates, apply the change and execute filtering with the new keywords
                keywords -> applyKeywordsAndExecuteFilter(
                        keywordSetter,
                        sourceKeywords,
                        new LinkedHashSet<>(keywords)));

        // Initialize the field with the current keywords
        field.setKeywords(List.copyOf(sourceKeywords));
        placeholder.getChildren().setAll(field.getRoot());

        // If keywords change externally (from CLI commands or other logic-triggered updates), re-set the field to
        // reflect the new state.
        sourceKeywords.addListener((SetChangeListener<? super String>) ignoredChange ->
                field.setKeywords(List.copyOf(sourceKeywords)));
    }

    /**
     * Attempts to apply a single field's keyword update and execute filtering.
     *
     * <p>Think of this as: "user edited a field -> try applying it".
     *
     * <p><b>Process:</b>
     * <ol>
     *   <li>Create a fresh {@link FilterDetails} snapshot copied from the current
     *       {@link ReadOnlyFilterDetails}.</li>
     *   <li>Apply the updated keyword set to that snapshot via {@code keywordSetter}.</li>
     *   <li>Execute filtering via {@link FilterExecutor}.</li>
     *   <li>If execution fails, return {@code sourceKeywords} (the last accepted keywords) so the UI can revert.</li>
     * </ol>
     *
     * @param keywordSetter method that sets the specified keyword field within a {@link FilterDetails}
     * @param sourceKeywords the last accepted keywords
     * @param updatedKeywords updated keywords proposed by the UI for this field
     * @return the keywords that should be displayed by the UI after validation/execution
     */
    private List<String> applyKeywordsAndExecuteFilter(
            KeywordSetter keywordSetter,
            ObservableSet<String> sourceKeywords,
            Set<String> updatedKeywords) {
        FilterDetails newFilterDetails = new FilterDetails(filterDetails);
        keywordSetter.set(newFilterDetails, updatedKeywords);

        try {
            filterExecutor.executeFilter(newFilterDetails);
        } catch (CommandException e) {
            // If there is a problem with the proposed keywords (e.g. invalid format, logic error), keep the
            // old keywords.
            return List.copyOf(sourceKeywords);
        }

        return List.copyOf(sourceKeywords);
    }
}
