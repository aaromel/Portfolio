package fi.tuni.trailweather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

/**
 *
 * @author Henri Sutinen
 */
public class SettingsViewController {
    @FXML
    private ListView<HBox> settingsListView;

    private List<String> selectedSettings = new ArrayList<>();
    
    // Singleton instance
    private static SettingsViewController instance;

    public void initialize() {
        // Create list
        ObservableList<HBox> settings = FXCollections.observableArrayList();

        // Add settings to HBox
        settings.add(createSetting("Temperature"));
        settings.add(createSetting("Rain probability"));
        settings.add(createSetting("Wind speed"));
        settings.add(createSetting("Humidity"));
        settings.add(createSetting("Rain volume"));

        // Set the list to listview
        settingsListView.setItems(settings);
        instance = this;
    }
    
    public static SettingsViewController getInstance(){
        if (instance == null) {
            instance = new SettingsViewController();
        }
        return instance;
    }
    
    private HBox createSetting(String settingName) {
        CheckBox checkBox = new CheckBox(settingName);
        checkBox.setId(settingName);
        checkBox.setDisable(true);
        checkBox.setOnAction(event -> handleCheckBoxClick(checkBox));

        HBox container = new HBox();
        // Add the contents to the container
        container.getChildren().addAll(checkBox);

        return container;
    }

    private void handleCheckBoxClick(CheckBox checkBox) {
        try {
            UserData.switchSettingState(checkBox.getId());
        } catch (IOException ex) {
            Logger.getLogger(SettingsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        selectedSettings.clear(); // Clear the list to avoid duplicates

        for (HBox hbox : settingsListView.getItems()) {
            for (javafx.scene.Node node : hbox.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox cb = (CheckBox) node;
                    if (cb.isSelected()) {
                        selectedSettings.add(cb.getId());
                    }
                }
            }
        }

        WeatherRenderer.getInstance().updateChart(selectedSettings);
    }

    @FXML
    private void handleClicks() {
        for (HBox hbox : settingsListView.getItems()) {
            for (javafx.scene.Node node : hbox.getChildren()) {
                if (node instanceof HBox) {
                    // Nested HBox, you can access its children similarly
                    HBox nestedHBox = (HBox) node;
                    for (javafx.scene.Node nestedNode : nestedHBox.getChildren()) {
                        if (nestedNode instanceof CheckBox) {
                            CheckBox checkBox = (CheckBox) nestedNode;
                            // Now you have access to the checkboxes
                            if (UserData.isChecked(checkBox.getId())) {
                                checkBox.setSelected(true);
                                selectedSettings.add(checkBox.getId());
                            }
                            checkBox.setDisable(false);
                        }
                    }
                } else if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                    // Now you have access to the checkboxes
                    if (UserData.isChecked(checkBox.getId())) {
                        checkBox.setSelected(true);
                        selectedSettings.add(checkBox.getId());
                    }
                    checkBox.setDisable(false);
                }
            }
        }

        WeatherRenderer.getInstance().updateChart(selectedSettings);
    }
}