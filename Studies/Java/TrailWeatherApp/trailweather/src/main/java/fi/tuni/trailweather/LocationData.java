package fi.tuni.trailweather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

/**
 * 
 * @author Henri Sutinen
 */
public class LocationData {

    public static String getLongLat(String address) {
        String result = "NO SUCH ADDRESS!";
        try {
            // String address = "1600 Amphitheatre Parkway, Mountain View, CA";
            System.out.println("Entered address: " + address);
            // Replace spaces with '+'
            address = address.replace(" ", "+");

            // Create the URL for the Nominatim API
            String apiUrl = "https://nominatim.openstreetmap.org/search?format=json&q=" + address;
            URL url = new URL(apiUrl);

            // Send a GET request to the API
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response
            String subString = response.substring(1);
            if (!subString.equals("]")) {
                JSONObject jsonResponse = new JSONObject(subString);

                // Extract latitude and longitude
                Double latitude = jsonResponse.getDouble("lat");
                Double longitude = jsonResponse.getDouble("lon");

                result = new String(latitude + ";" + longitude);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // Own code ->

    public static String getLocationName(double latitude, double longitude) {
        try {
            // Url for the Nominatim API
            String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + latitude + "&lon=" + longitude
                    + "&zoom=18&addressdetails=1";
            URL apiUrl = new URL(url);

            // Send a GET request to the API
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");

            // Read the response
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (!jsonResponse.has("address")) {
                    return "Address not found";
                }
                JSONObject addressObject = jsonResponse.getJSONObject("address");

                // Form the location string based on the keys address object contains
                String locationString = "";
                if (addressObject.has("road")) {
                    locationString = addressObject.getString("road");
                    if (addressObject.has("house_number")) {
                        locationString += " " + addressObject.getString("house_number");
                    }
                    if (addressObject.has("city"))
                        locationString += ", " + addressObject.getString("city");
                    else {
                        if (addressObject.has("town")) {
                            locationString += ", " + addressObject.getString("town");
                        }
                    }
                    return locationString;
                } else {
                    String displayName = jsonResponse.getString("display_name");
                    String[] splitDisplayName = displayName.split(",");
                    locationString = splitDisplayName[0] + ", " + splitDisplayName[1];
                    return locationString;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Address not found";
    }
}

// <- Own code
