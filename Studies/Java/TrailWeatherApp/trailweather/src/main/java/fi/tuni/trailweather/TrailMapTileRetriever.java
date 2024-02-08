package fi.tuni.trailweather;

import com.gluonhq.maps.tile.TileRetriever;
import java.util.concurrent.CompletableFuture;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;

public class TrailMapTileRetriever implements TileRetriever {

    public static enum MapType {
        MTB("https://mtb-tileserver.trailmap.fi/tiles/"),
        MTB_HIGH_RES("https://mtb-tileserver.trailmap.fi/tiles@2x/"),
        MTB_WINTER("https://mtb-tileserver.trailmap.fi/tiles_winter/"),
        MTB_3D("https://tilestrata.trailmap.fi/d3-mtbmap-cache/3d/"),
        MTB_3D_HIGH_RES("https://tilestrata.trailmap.fi/d3-mtbmap-cache/3d/"),
        ORIENTEERING("https://wmts.mapant.fi/wmts_EPSG3857.php?"),
        NLS_TOPO("https://tiles.kartat.kapsi.fi/peruskartta/"),
        NLS_ORTHO("https://tiles.kartat.kapsi.fi/ortokuva/");

        private final String baseUrl;

        MapType(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        @Override
        public String toString() {
            return name().replace("_", " "); // Replace underscores with spaces
        }
    }

    private static TrailMapTileRetriever instance;

    public static synchronized TrailMapTileRetriever getInstance() {
        if (instance == null) {
            instance = new TrailMapTileRetriever();
        }
        return instance;
    }

    public static TrailMapTileRetriever provider() {
        return getInstance();
    }

    private MapType currentMapType = MapType.MTB;
    private final String httpAgent;

    private TrailMapTileRetriever() {
        String agent = System.getProperty("http.agent");
        if (agent == null) {
            agent = "(" + System.getProperty("os.name") + " / " + System.getProperty("os.version") + " / " + System.getProperty("os.arch") + ")";
        }
        httpAgent = "Gluon Maps/2.0.0 " + agent;
        System.setProperty("http.agent", httpAgent);
    }

    private String buildImageUrlString(int zoom, long i, long j) {
        String fileExtension;
        switch (currentMapType) {
            case MTB:
            case MTB_HIGH_RES:
            case MTB_WINTER:
            case MTB_3D:
            case MTB_3D_HIGH_RES:
                fileExtension = ".png";
                break;
            case NLS_TOPO:
            case NLS_ORTHO:
                fileExtension = ".jpg";
                break;
            case ORIENTEERING:
                String zxy = String.format("z=%s&x=%s&y=%s", zoom, i, j);
                return currentMapType.getBaseUrl() + zxy;
            default:
                // Default to ".png" if the map type is not recognized
                fileExtension = ".png";
                break;
        }
        return currentMapType.getBaseUrl() + zoom + "/" + i + "/" + j + fileExtension;
    }

    @Override
    public CompletableFuture<Image> loadTile(int zoom, long i, long j) {
        String urlString = buildImageUrlString(zoom, i, j);
        return CompletableFuture.completedFuture(new Image(urlString, true));
    }

    public void setCurrentMapType(MapType mapType) {
        this.currentMapType = mapType;
    }

    public void setCurrentMapType(RadioButton selectedRadioButton) {
        String buttonText = selectedRadioButton.getText();
        for (MapType mapType : MapType.values()) {
            if (mapType.toString().equalsIgnoreCase(buttonText)) {
                this.currentMapType = mapType;
                break;
            }
        }
    }
}
