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
     * Fills inner placeholders with reusable field components.
     */
    private void fillInnerParts() {
        FilterPanelField nameFilterField = new FilterPanelField(
                "Search by Name",
                "E.g: Alex",
                this::handleNameKeywordsChanged);

        nameFilterFieldPlaceholder.getChildren().setAll(nameFilterField.getRoot());

        filterDetails.getNameKeywords().addListener(
                (SetChangeListener<? super String>) change ->
                        nameFilterField.setKeywords(List.copyOf(filterDetails.getNameKeywords())));

        // Trigger the listener in ModelManager
        filterDetails.set(newFilterDetails);

        nameFilterField.clear();
    }
}
