package seedu.address.ui.filter;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.ui.UiPart;

/**
 * Reusable combo-box based filter field in the {@code FilterPanel}.
 */
public class FilterPanelComboBox extends UiPart<Region> {
    private static final String FXML = "FilterPanelComboBox.fxml";

    private final List<String> currentKeywords;
    private final KeywordsChangedHandler onKeywordsChanged;

    @FXML
    private Label titleLabel;
    @FXML
    private ComboBox<String> keywordComboBox;
    @FXML
    private Label keywordsLabel;
    @FXML
    private FlowPane keywordsFlowPane;

    /**
     * Creates a reusable combo-box filter field section.
     *
     * @param title             The title of this filter field, e.g. "Search by Gender".
     * @param promptText        The prompt text to show in the combo box editor.
     * @param options           The list of selectable suggestions shown in the combo box.
     * @param onKeywordsChanged The callback that validates and applies edited keywords.
     */
    public FilterPanelComboBox(String title, String promptText, List<String> options,
                               KeywordsChangedHandler onKeywordsChanged) {
        super(FXML);
        requireNonNull(title);
        requireNonNull(promptText);
        requireNonNull(options);
        requireNonNull(onKeywordsChanged);

        this.onKeywordsChanged = onKeywordsChanged;
        this.currentKeywords = new ArrayList<>();

        titleLabel.setText(title);
        keywordComboBox.setEditable(true);
        keywordComboBox.setPromptText(promptText);
        keywordComboBox.getItems().setAll(options);
    }

    /**
     * Replaces the current list of keywords and redraws this field's FlowPane tags.
     */
    public void setKeywords(List<String> updatedKeywords) {
        requireNonNull(updatedKeywords);
        applyValidatedKeywords(updatedKeywords);
    }

    /**
     * Handles add-keyword on combo box action (selection or Enter in editor).
     */
    @FXML
    private void handleFieldEntered() {
        String keyword = keywordComboBox.getEditor().getText();
        requireNonNull(keyword);

        String trimmedKeyword = keyword.trim();
        if (trimmedKeyword.isEmpty() || currentKeywords.contains(trimmedKeyword)) {
            clearInput();
            return;
        }

        List<String> proposedKeywords = new ArrayList<>(currentKeywords);
        proposedKeywords.add(trimmedKeyword);

        List<String> validatedKeywords = onKeywordsChanged.handle(proposedKeywords);
        applyValidatedKeywords(validatedKeywords);
        clearInput();
    }

    private void clearInput() {
        keywordComboBox.getSelectionModel().clearSelection();
        keywordComboBox.getEditor().clear();
    }

    private void applyValidatedKeywords(List<String> validatedKeywords) {
        currentKeywords.clear();

        validatedKeywords.stream()
                .map(String::trim)
                .filter(keyword -> !keyword.isEmpty())
                .filter(keyword -> !currentKeywords.contains(keyword))
                .forEach(currentKeywords::add);

        keywordsFlowPane.getChildren().clear();
        currentKeywords.forEach(keyword -> keywordsFlowPane.getChildren()
                .add(new FilterPanelTag(keyword, this::handleDeleteTag).getRoot()));
    }

    private void handleDeleteTag(String tagToDelete) {
        List<String> proposedKeywords = new ArrayList<>(currentKeywords);
        if (!proposedKeywords.remove(tagToDelete)) {
            return;
        }

        List<String> validatedKeywords = onKeywordsChanged.handle(List.copyOf(proposedKeywords));
        applyValidatedKeywords(validatedKeywords);
    }

    /**
     * Handler for when the keywords in this field are edited.
     */
    @FunctionalInterface
    public interface KeywordsChangedHandler {
        List<String> handle(List<String> keywords);
    }
}

