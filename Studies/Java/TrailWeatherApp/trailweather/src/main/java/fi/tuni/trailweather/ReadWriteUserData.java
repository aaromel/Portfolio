package fi.tuni.trailweather;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author Henri Sutinen
 */
public class ReadWriteUserData {

    public static void writeFile() throws IOException {
        Map<String, Pair<Double, Double>> locationsMap = UserData.getSavedLocations();
        Map<String, Boolean> settingsMap = UserData.getUserSettings();
        JsonObject dataToSave = new JsonObject();
        JsonArray locationArray = new JsonArray();
        for (var location : locationsMap.entrySet()) {
            JsonObject locationObject = new JsonObject();
            locationObject.addProperty("name", location.getKey());
            locationObject.addProperty("x", location.getValue().getKey());
            locationObject.addProperty("y", location.getValue().getValue());
            locationArray.add(locationObject);
        }
        dataToSave.add("locations", locationArray);

        JsonObject settingsObject = new JsonObject();
        for (var setting : settingsMap.entrySet()) {
            settingsObject.addProperty(setting.getKey(), setting.getValue());
        }
        dataToSave.add("settings", settingsObject);

        File fileToWrite = new File("usersettings.json");
        FileWriter fileWriter = new FileWriter(fileToWrite);
        Gson gson = new Gson();
        gson.toJson(dataToSave, fileWriter);
        fileWriter.close();
    }

    public static JsonObject readFile() throws FileNotFoundException {
        JsonObject jsonObject = new JsonObject();
        File file = new File("usersettings.json");

        if(!file.exists()) { 
            file = new File("trailweather/usersettings.json");
        }

        JsonParser parser = new JsonParser();
        BufferedReader br = new BufferedReader(new FileReader(file));
        JsonElement root = parser.parse(br);
        jsonObject = root.getAsJsonObject();
        return jsonObject;
    }
}
