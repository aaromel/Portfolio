package fi.tuni.trailweather;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Jyri Hakala
 */
public class MapController {

    private static MapController instance;

    // Tampere lat = 61.4991, lon = 23.7871
    final MapPoint tampere = new MapPoint(61.4991, 23.7871);

    // Declare as attribute to allow modification
    private static MapView mapView;

    @FXML
    private Pane mapPane;

    public static synchronized MapController getInstance() {
        if (instance == null) {
            instance = new MapController();
        }
        return instance;
    }

    public void flyToLocation(double latitude, double longitude) {
        MapPoint locationPoint = new MapPoint(latitude, longitude);
        getInstance().mapView.flyTo(0, locationPoint, 0.1);
        getInstance().mapView.setZoom(10);
    }

    public void flyToLocation(MapPoint mp) {
        getInstance().mapView.flyTo(0, mp, 0.1);
        getInstance().mapView.setZoom(10);
    }

    /**
     * Initializes the controller class.
     */
    public void initialize() {
        mapView = createMapView();
        bindHandlers(mapView);
        mapPane.getChildren().add(mapView);
        instance = this;

    }

    private MapView createMapView() {
        MapView newMapView = new MapView();
        newMapView.setPrefSize(555, 400);
        newMapView.flyTo(0, tampere, 0.1);
        newMapView.setZoom(10);

        return newMapView;
    }

    public void refresh() {
        MapView newMapView = new MapView();
        newMapView.setPrefSize(555, 400);
        newMapView.setCenter(mapView.getCenter());
        newMapView.setZoom(mapView.getZoom());
        bindHandlers(newMapView);

        getInstance().mapPane.getChildren().clear();
        getInstance().mapPane.getChildren().add(newMapView);
        mapView = newMapView;
    }

    // Own code ->

    private void bindHandlers(MapView mv) {
        mv.setOnMouseClicked(event -> {
            // Check if doubleclick
            if (event.getClickCount() == 2) {
                // Get coordinates of the click
                double clickX = event.getX();
                double clickY = event.getY();

                // Get the location of the click
                MapPoint clickedPoint = mv.getMapPosition(clickX, clickY);

                // Get the address of the location
                String address = LocationData.getLocationName(clickedPoint.getLatitude(),
                        clickedPoint.getLongitude());
                System.out.println("Address: " + address);

                // Enable the save place button if address is found
                if (!address.equals("Address not found")) {
                    GenericUIController.getInstance().updateSelection(clickedPoint, address);
                } else {
                    GenericUIController.getInstance().resetPlaceSelection();
                }
            }
        });
    }

    // <- Own code
}
