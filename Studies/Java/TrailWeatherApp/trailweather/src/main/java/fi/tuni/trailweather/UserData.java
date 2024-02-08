package fi.tuni.trailweather;

import com.google.gson.JsonArray;
import java.util.Map;
import javafx.util.Pair;
import com.google.gson.JsonObject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Henri Sutinen
 */
public class UserData {
    private static Map<String, Pair<Double, Double>> savedLocations;
    private static Map<String, Boolean> userSettings;

    public UserData() throws FileNotFoundException, IOException {
        JsonObject data = ReadWriteUserData.readFile();
        savedLocations = new HashMap<>();
        parseLocations(data);
        Map<String, Boolean> settings = parseSettings(data);
        this.userSettings = settings;
    }

    public void parseLocations(JsonObject jsonObject) {
        JsonArray locationsArray = jsonObject.getAsJsonArray("locations");
        for (var location : locationsArray) {
            Double xCord = location.getAsJsonObject().get("x").getAsDouble();
            Double yCord = location.getAsJsonObject().get("y").getAsDouble();
            Pair<Double, Double> coordinates = new Pair<>(xCord, yCord);
            String name = location.getAsJsonObject().get("name").getAsString();
            addNewLocation(name, coordinates);
        }
    }

    public Map<String, Boolean> parseSettings(JsonObject jsonObject) {
        Map<String, Boolean> result = new HashMap<>();
        JsonObject settingsObject = jsonObject.getAsJsonObject("settings");
        for (var singleSetting : settingsObject.entrySet()) {
            result.put(singleSetting.getKey(), singleSetting.getValue().getAsBoolean());
            
        }
        return result;
    }

    public static void updateUserSettings() throws IOException {
        ReadWriteUserData.writeFile();
    }

    public static void addNewLocation(String locationName, Pair<Double, Double> location) {
        Double xCord = location.getKey();
        Double yCord = location.getValue();
        Pair coordinates = new Pair<>(xCord, yCord);
        savedLocations.put(locationName, coordinates);
    }

    public static void deleteLocation(String locationName) throws IOException {
        if (savedLocations.containsKey(locationName)) {
            savedLocations.remove(locationName);
            updateUserSettings();
        }
    }

    public static Map<String, Pair<Double, Double>> getSavedLocations() {
        return savedLocations;
    }

    public static Map<String, Boolean> getUserSettings() {
        return userSettings;
    }
    
    public static Boolean isChecked(String name){
        Boolean result = false;
        if(userSettings.containsKey(name)){
            result = userSettings.get(name);
        }
        return result;
    }
    
    public static void switchSettingState(String settingName) throws IOException{
        if(userSettings.containsKey(settingName)){
            if(userSettings.get(settingName) == true){
                userSettings.replace(settingName, false);
            } else {
                userSettings.replace(settingName, true);
            }
        }
        updateUserSettings();
    }
}
