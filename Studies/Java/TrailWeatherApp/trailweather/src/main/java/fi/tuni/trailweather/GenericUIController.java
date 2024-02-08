package fi.tuni.trailweather;

import com.gluonhq.maps.MapPoint;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

// Own code ->

/**
 * Controller for the UI
 *
 *
 * @author Aaro Melchy
 */
public class GenericUIController {

    @FXML
    private TextField searchText;
    @FXML
    private Label notification;
    @FXML
    private Button savePlaceButton;
    @FXML
    private Label locationLabel;

    @FXML
    private Label temperatureLabel;
    @FXML
    private Label humidityLabel;
    @FXML
    private Label pressureLabel;
    @FXML
    private Label windspeedLabel;
    @FXML
    private Label rainVolumeLabel;

    @FXML
    private Button forecastButton;
    @FXML
    private Stage popUp;

    private static GenericUIController instance;
    private MapPoint selectedLocationPoint = null;
    private String selectedLocationAddress = null;

    public static synchronized GenericUIController getInstance() {
        if (instance == null) {
            instance = new GenericUIController();
        }
        return instance;
    }

    public void initialize() {
        instance = this;
        // Set the button disabled on initialization
        updateSaveButtonState(true);
        double lat = MapController.getInstance().tampere.getLatitude();
        double lon = MapController.getInstance().tampere.getLongitude();
        setWeatherInfo(lat, lon);

        forecastButton.setOnMousePressed(event -> {
            if (popUp.isShowing()) {
                popUp.hide();
            } else {
                popUp.show();
            }
        });

        searchText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleClick();
            }
        });

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/forecast.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            popUp = new Stage();
            popUp.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            popUp.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClick() {
        try {
            String locData = LocationData.getLongLat(searchText.getText());
            if (locData.equals("NO SUCH ADDRESS!")) {
                showNotification("Cannot find location!", Color.RED);
            } else {
                String[] latLon = locData.split(";");
                MapPoint location = new MapPoint(Double.parseDouble(latLon[0]),
                        Double.parseDouble(latLon[1]));
                MapController.getInstance().flyToLocation(location);

                resetPlaceSelection();

                setWeatherInfo(Double.parseDouble(latLon[0]), Double.parseDouble(latLon[1]));
                WeatherRenderer.getInstance().setForecastLocation(Double.parseDouble(latLon[0]),
                        Double.parseDouble(latLon[1]));
            }
        } catch (Exception e) {
            System.out.println("Error in address search!");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSavePlace() {
        if (selectedLocationPoint != null && selectedLocationAddress != null) {
            // Dialog for entering the name of the location
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Save Place");
            dialog.setHeaderText("Enter a name for the location:");
            dialog.setContentText("Name:");

            dialog.showAndWait().ifPresent(name -> {
                System.out.println(name);
                try {
                    saveLocation(name);
                } catch (IOException ex) {
                    Logger.getLogger(MapController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    public void setWeatherInfo(double latitude, double longitude) {
        WeatherData wd = WeatherDataController.getCurrentWeatherData(latitude, longitude);
        getInstance().temperatureLabel.setText("Temperature: " + String.valueOf(wd.getTemperature()) + "°C");
        getInstance().humidityLabel.setText("Humidity: " + String.valueOf(wd.getHumidity()) + "%");
        getInstance().pressureLabel.setText("Pressure: " + String.valueOf(wd.getPressure()) + " hPa");
        getInstance().windspeedLabel.setText("Windspeed: " + String.valueOf(wd.getWindSpeed()) + " m/s");
        getInstance().rainVolumeLabel.setText("Rain volume: " + String.valueOf(wd.getRainVolume()) + " mm");
    }

    private void saveLocation(String name) throws IOException {
        String locationName;

        // If no name is given, use the address as the name
        if (name.length() == 0) {
            locationName = selectedLocationAddress;
        } else {
            // Form a string with the name and address
            locationName = name + " (" + selectedLocationAddress + ")";
        }

        UserData.addNewLocation(locationName,
                new Pair<>(selectedLocationPoint.getLatitude(), selectedLocationPoint.getLongitude()));

        ListViewController.getInstance().updatePlacesList();

        resetPlaceSelection();
        UserData.updateUserSettings();

        showNotification("Saved new place!", Color.GREEN);
    }

    private void updateSaveButtonState(boolean disabled) {
        getInstance().savePlaceButton.setDisable(disabled);
    }

    public void resetPlaceSelection() {
        getInstance().selectedLocationPoint = null;
        getInstance().selectedLocationAddress = null;
        updateSaveButtonState(true);
    }

    public void setLocationLabel(String location) {
        getInstance().locationLabel.setText(location);
    }

    public void updateSelection(MapPoint selectedPoint, String address) {
        getInstance().selectedLocationPoint = selectedPoint;
        getInstance().selectedLocationAddress = address;
        getInstance().updateSaveButtonState(false);
        getInstance().setWeatherInfo(selectedPoint.getLatitude(), selectedPoint.getLongitude());
        getInstance().locationLabel.setText(address);
        WeatherRenderer.getInstance().setForecastLocation(selectedPoint.getLatitude(), selectedPoint.getLongitude());
    }

    private void showNotification(String message, Color color) {
        getInstance().notification.setText(message);
        getInstance().notification.setTextFill(color);
        getInstance().notification.setFont(Font.font("System", FontWeight.NORMAL, 14));

        // Set the duration of the notification to 4 seconds
        Duration notificationDuration = Duration.seconds(4);

        // Event handler to hide the notification
        EventHandler<ActionEvent> hideNotification = event -> {
            getInstance().notification.setText("");
        };

        // Create KeyFrame for hiding the notification
        KeyFrame hideKeyFrame = new KeyFrame(notificationDuration, hideNotification);

        // Create a timeline based on the keyframe
        Timeline notificationTimeline = new Timeline(hideKeyFrame);

        // Start the timeline
        notificationTimeline.play();
    }
}

// <- Own code