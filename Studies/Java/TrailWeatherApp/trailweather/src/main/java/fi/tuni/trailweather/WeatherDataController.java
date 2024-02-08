package fi.tuni.trailweather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Paavo Jyrkiäinen
 */
public class WeatherDataController {

    // For testing purposes
    private static void printWeatherData(ArrayList<WeatherData> weatherArray) {
        System.out.println("*-*-*-*CURRENT WEATHERDATA*-*-*-*");
        for (int i = 0; i < weatherArray.size(); i++) {
            WeatherData current = weatherArray.get(i);
            System.out.println("Timestamp: " + current.getTimeStamp());
            System.out.println("Location: " + current.getLocationName());
            System.out.println("Latitude: " + current.getLatitude());
            System.out.println("Longitude: " + current.getLongitude());
            System.out.println("Temperature: " + current.getTemperature() + "°C");
            System.out.println("Pressure: " + current.getPressure() + " hPa");
            System.out.println("Humidity: " + current.getHumidity() + "%");
            System.out.println("Windspeed: " + current.getWindSpeed() + " m/s");
            System.out.println("Rain probability: " + current.getRainProbability());
            System.out.println("Rain volume: " + current.getRainVolume() + " mm");
            System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        }
    }

    public static ArrayList<WeatherData> getForecastWeatherData(double lat, double lon) {
        try {
            // Weather api key
            final String apiKey = "04b847ddaab9fa7675adbe6fff58cd7b";

            // Create the URL for the weather API
            String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid="
                    + apiKey + "&units=metric";
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

            String subString = response.substring(0);
            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(subString);

            JSONArray jsonArray = jsonResponse.getJSONArray("list");

            ArrayList<WeatherData> weatherArray = new ArrayList<>();
            String locationName = jsonResponse.getJSONObject("city").getString("name");
            double latitude = jsonResponse.getJSONObject("city").getJSONObject("coord").getDouble("lat");
            double longitude = jsonResponse.getJSONObject("city").getJSONObject("coord").getDouble("lon");
            // Currently gets the next 6 weather forecasts (every 3hrs)
            // Free api provides forecast every 3hrs for 5 days max
            for (int i = 0; i < 40; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Integer dateTime = obj.getInt("dt");

                // Format timestamp to readable form
                Date date = new Date(TimeUnit.MILLISECONDS.convert(dateTime, TimeUnit.SECONDS));
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");
                String timeStamp = sdf.format(date);

                double temp = obj.getJSONObject("main").getDouble("temp");
                int temperature = (int) Math.round(temp);

                double pressure = obj.getJSONObject("main").getDouble("pressure");
                double humidity = obj.getJSONObject("main").getDouble("humidity");
                double windSpeed = obj.getJSONObject("wind").getDouble("speed");
                // Multiply to get percentage
                double rainProbability = obj.getDouble("pop") * 100;
                double rainVolume;
                try {
                    if (obj.has("rain")) {
                        rainVolume = obj.getJSONObject("rain").getDouble("3h");
                    } else if (obj.has("snow")) {
                        rainVolume = obj.getJSONObject("snow").getDouble("3h");
                    } else {
                        rainVolume = 0.0;
                    }
                } catch (Exception e) {
                    rainVolume = 0.0;
                }

                WeatherData weather = new WeatherData(locationName, timeStamp, latitude, longitude, temperature,
                        pressure, humidity, windSpeed, rainProbability, rainVolume);
                weatherArray.add(weather);
                // printWeatherData(weatherArray);
            }
            return weatherArray;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static WeatherData getCurrentWeatherData(double lat, double lon) {
        try {
            // Weather api key
            final String apiKey = "04b847ddaab9fa7675adbe6fff58cd7b";

            // Create the URL for the weather API
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid="
                    + apiKey + "&units=metric";
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

            String subString = response.substring(0);
            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(subString);

            String locationName = jsonResponse.getString("name");
            double latitude = jsonResponse.getJSONObject("coord").getDouble("lat");
            double longitude = jsonResponse.getJSONObject("coord").getDouble("lon");

            Integer dateTime = jsonResponse.getInt("dt");

            // Format timestamp to readable form
            Date date = new Date(TimeUnit.MILLISECONDS.convert(dateTime, TimeUnit.SECONDS));
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            String timeStamp = sdf.format(date);

            double temp = jsonResponse.getJSONObject("main").getDouble("temp");
            int temperature = (int) Math.round(temp);

            double pressure = jsonResponse.getJSONObject("main").getDouble("pressure");
            double humidity = jsonResponse.getJSONObject("main").getDouble("humidity");
            double windSpeed = jsonResponse.getJSONObject("wind").getDouble("speed");
            double rainVolume;
            try {
                if (jsonResponse.has("rain")) {
                    rainVolume = jsonResponse.getJSONObject("rain").getDouble("1h");
                } else if (jsonResponse.has("snow")) {
                    rainVolume = jsonResponse.getJSONObject("snow").getDouble("1h");
                } else {
                    rainVolume = 0.0;
                }
            } catch (Exception e) {
                rainVolume = 0.0;
            }
            // Not applicable for current weather
            double rainProbability = 0.0;
            WeatherData weather = new WeatherData(locationName, timeStamp, latitude, longitude, temperature,
                    pressure, humidity, windSpeed, rainProbability, rainVolume);

            return weather;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
