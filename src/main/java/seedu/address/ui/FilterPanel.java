package seedu.address.ui;

import java.util.Set;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.FilterDetails;
import seedu.address.ui.filter.FilterPanelTag;

/**
 * Panel containing the list of filtering and sorting options.
 */
public class FilterPanel extends UiPart<Region> {
    private static final String FXML = "FilterPanel.fxml";
    private final ObjectProperty<FilterDetails> filterDetails;

    @FXML
    private TextField nameFilterField;
    @FXML
    private FlowPane nameTagsFlowPane;
    @FXML
    private TextField phoneNumberFilterField;
    @FXML
    private TextField emailFilterField;
    @FXML
    private TextField studentIdFilterField;
    @FXML
    private TextField roomNumberFilterField;
    @FXML
    private TextField majorFilterField;
    @FXML
    private TextField emergencyContactFilterField;
    @FXML
    private ComboBox<String> yearFilterComboBox;
    @FXML
    private ComboBox<String> genderFilterComboBox;
    @FXML
    private FontIcon filterIcon;
    @FXML
    private FontIcon sortIcon;

    /**
     * Creates a {@code FilterPanel} with the given {@code ObjectProperty<FilterDetails>}.
     */
    public FilterPanel(ObjectProperty<FilterDetails> filterDetails) {
        super(FXML);
        this.filterDetails = filterDetails;
        fillInnerParts();
    }

    /**
    * Fills the inner parts of the FilterPanel, such as setting up event handlers for the filter fields and
    */
    private void fillInnerParts() {
    }
    /*
    * Handles the event when the user presses 'Enter' in the name filter field.
    * Splits the input into individual keywords and displays them as tags in the UI.
     */
    @FXML
    private void handleNameFieldEntered() {
        nameTagsFlowPane.getChildren().clear();
        String nameFilterText = nameFilterField.getText();
        if (nameFilterText.trim().isEmpty()) {
            return;
        }
        Set<String> nameFilterKeywordsSet = StringUtil.splitSentenceIntoWords(nameFilterText);

        // Display each keyword as a tag in the UI
        nameFilterKeywordsSet.forEach(tag -> nameTagsFlowPane.getChildren()
                .add(new FilterPanelTag(tag).getRoot()));

        // Create a new FilterDetails with updated name keywords
        FilterDetails newFilterDetails = new FilterDetails(filterDetails.get());
        newFilterDetails.setNameKeywords(nameFilterKeywordsSet);

        // Trigger the listener in ModelManager
        filterDetails.set(newFilterDetails);

        nameFilterField.clear();
    }
}
