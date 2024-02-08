module fi.tuni.trailweather {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.gluonhq.maps;
    requires org.json;
    requires com.google.gson;
    requires org.jfree.chart.fx;
    requires java.desktop;

    uses com.gluonhq.maps.tile.TileRetriever;
    provides com.gluonhq.maps.tile.TileRetriever with fi.tuni.trailweather.TrailMapTileRetriever;
    
    opens fi.tuni.trailweather to javafx.fxml;
    exports fi.tuni.trailweather;
}
