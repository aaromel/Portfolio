package fi.tuni.trailweather;

public class WeatherData {

    String locationName;
    String timeStamp;
    double latitude;
    double longitude;
    int temperature;
    double pressure;
    double humidity;
    double windSpeed;
    double rainProbability;
    double rainVolume;

    public WeatherData(String loc,
            String time,
            double lat,
            double lon,
            int temp,
            double pres,
            double humi,
            double wind,
            double rainProb,
            double rainVol) {
        locationName = loc;
        timeStamp = time;
        latitude = lat;
        longitude = lon;
        temperature = temp;
        pressure = pres;
        humidity = humi;
        windSpeed = wind;
        rainProbability = rainProb;
        rainVolume = rainVol;
    }

    // Getters
    public String getLocationName() {
        return locationName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getTemperature() {
        return temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getRainProbability() {
        return rainProbability;
    }

    public double getRainVolume() {
        return rainVolume;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Timestamp: %s\n", this.getTimeStamp()));
        sb.append(String.format("Location: %s\n", this.getLocationName()));
        sb.append(String.format("Latitude: %s\n", this.getLatitude()));
        sb.append(String.format("Longitude: %s\n", this.getLongitude()));
        sb.append(String.format("Temperature: %s\n", this.getTemperature()));
        sb.append(String.format("Pressure: %s\n", this.getPressure()));
        sb.append(String.format("Humidity: %s\n", this.getHumidity()));
        sb.append(String.format("Windspeed: %s\n", this.getWindSpeed()));
        sb.append(String.format("Rain probability:: %s\n", this.getRainProbability()));
        sb.append(String.format("Rain volume: %s\n", this.getRainVolume()));

        return sb.toString();
    }
}
