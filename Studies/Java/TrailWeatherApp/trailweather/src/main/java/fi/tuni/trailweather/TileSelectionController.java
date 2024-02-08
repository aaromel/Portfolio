package fi.tuni.trailweather;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 *
 * @author Jyri Hakala
 */
public class TileSelectionController {

    @FXML
    private ToggleGroup tileGroup;

    @FXML
    private TrailMapTileRetriever trailMapTileRetriever;

    @FXML
    private MapController mapController;

    @FXML
    private void updateMapType() {
        RadioButton selectedRadioButton = (RadioButton) tileGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            trailMapTileRetriever.setCurrentMapType(selectedRadioButton);

            mapController.refresh();
        }
    }

    @FXML
    private void initialize() {
        trailMapTileRetriever = TrailMapTileRetriever.getInstance();
        mapController = MapController.getInstance();
    }
}
