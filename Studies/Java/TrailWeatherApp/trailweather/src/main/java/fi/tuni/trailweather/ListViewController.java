package fi.tuni.trailweather;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.Pair;

// Own code ->

/**
 * FXML Controller class for the places listview.
 *
 *
 * @author Aaro Melchy
 */
public class ListViewController {

    @FXML
    private ListView<HBox> placesListView;

    private ObservableList<HBox> places;

    // Singleton instance
    private static ListViewController instance;

    public ListViewController() {
        // Initialize places list
        places = FXCollections.observableArrayList();
    }

    // Singleton getter
    public static ListViewController getInstance() {
        if (instance == null) {
            instance = new ListViewController();
        }
        return instance;
    }

    public static ListViewController provider() {
        return getInstance();
    }

    public void initialize() {
        ListViewController listViewController = ListViewController.getInstance();

        listViewController.updatePlacesList();

        placesListView.setItems(listViewController.getPlaces());
    }

    public ObservableList<HBox> getPlaces() {
        return places;
    }

    // Update the listview with saved locations.
    public void updatePlacesList() {
        places.clear(); // Clear the list

        // Add the saved locations to the list
        Map<String, Pair<Double, Double>> savedLocations = UserData.getSavedLocations();
        if (savedLocations != null) {
            for (Map.Entry<String, Pair<Double, Double>> entry : savedLocations.entrySet()) {
                places.add(createPlace(entry.getKey(), entry.getValue()));
            }
        }
    }

    // Create a place container holding the elements
    private HBox createPlace(String placeName, Pair<Double, Double> coordinates) {
        Label nameLabel = new Label(placeName);
        Button goButton = new Button("Go");
        Button deleteButton = new Button("Delete");
        HBox container = new HBox();

        // Align the contents
        container.setAlignment(Pos.CENTER_LEFT);

        // Create a spacer that fills the empty space
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Add the contents to the container
        container.getChildren().addAll(nameLabel, spacer, goButton, deleteButton);

        // Move map to the location when go button is pressed
        goButton.setOnAction(event -> {
            updateLocation(placeName, coordinates);
        });

        // Delete the place when delete button is pressed
        deleteButton.setOnAction(event -> {
            handleDelete(placeName);
        });

        return container;
    }

    private void updateLocation(String placeName, Pair<Double, Double> coordinates) {
        if (MapController.getInstance() != null && GenericUIController.getInstance() != null) {
            // Move the map to the location and update UI
            MapController.getInstance().flyToLocation(coordinates.getKey(), coordinates.getValue());
            GenericUIController.getInstance().setLocationLabel(placeName);
            GenericUIController.getInstance().setWeatherInfo(coordinates.getKey(), coordinates.getValue());
        } else {
            System.out.println("Controllers are null");
        }
    }

    private void handleDelete(String placeName) {
        // Show confirmation pop up
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to delete this place?");

        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Wait for user input
        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    // Delete the place if user pressed OK
                    UserData.deleteLocation(placeName);
                } catch (IOException ex) {
                    Logger.getLogger(ListViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                updatePlacesList();
            }
        });
    }
}

// <- Own code