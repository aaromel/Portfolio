package fi.tuni.trailweather;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

// Own code ->

public class WeatherRenderer {

    @FXML
    private ChartViewer chartViewer;

    @FXML
    private HBox dateButtonContainer;

    ArrayList<WeatherData> forecastData;

    private List<String> selectedSettings;

    private String selectedDate;

    private static WeatherRenderer instance;

    public static synchronized WeatherRenderer getInstance() {
        if (instance == null) {
            instance = new WeatherRenderer();
        }
        return instance;
    }

    public void initialize() {
        selectedSettings = new ArrayList<>();
        selectedSettings.add("Temperature");
        selectedSettings.add("Humidity");
        selectedSettings.add("Wind speed");
        selectedSettings.add("Rain probability");
        selectedSettings.add("Rain volume");
        createDateButtons();
        setForecastLocation(61.4991, 23.7871);
        instance = this;
    }

    private void createDateButtons() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");
        Calendar calendar = Calendar.getInstance();

        // Initialize the selected date to today
        selectedDate = dateFormat.format(new Date());

        for (int i = 1; i <= 5; i++) {
            Button dateButton = new Button();
            dateButtonContainer.getChildren().add(dateButton);

            // Set the button text with the date
            Date date = calendar.getTime();
            dateButton.setText(dateFormat.format(date));

            // Add event handler for the button
            dateButton.setOnAction(event -> handleDateButtonClick(dateButton));

            // Add one day to the calendar
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void handleDateButtonClick(Button clickedButton) {
        // Update the selected date
        String buttonText = clickedButton.getText();
        selectedDate = buttonText;

        // Update the chart
        updateChart(selectedSettings);

    }

    public void setForecastLocation(double x, double y) {
        forecastData = WeatherDataController.getForecastWeatherData(x, y);

        var plot = buildPlot();
        var chart = buildChart(plot);

        chartViewer.setChart(chart);
    }

    public void updateChart(List<String> settings) {
        selectedSettings = settings;

        var plot = buildPlot();
        var chart = buildChart(plot);

        chartViewer.setChart(chart);
    }

    private JFreeChart buildChart(CategoryPlot plot) {
        return new JFreeChart("Forecast", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
    }

    private CategoryPlot buildPlot() {
        CategoryPlot plot = new CategoryPlot();
        NumberAxis secondaryAxis = createSecondaryAxis(Color.blue);

        // Add the selected settings to the plot
        for (String setting : selectedSettings) {
            switch (setting) {
                case "Temperature":
                    plot.setDataset(0, getTemperatures());
                    plot.setRenderer(0, createLineRenderer(Color.red));
                    break;
                case "Humidity":
                    plot.setDataset(1, getHumidities());
                    plot.setRenderer(1, createLineRenderer(Color.green));
                    break;
                case "Wind speed":
                    plot.setDataset(2, getWindSpeeds());
                    plot.setRenderer(2, createLineRenderer(Color.black));
                    break;
                case "Rain probability":
                    plot.setDataset(3, getRainProbabilities());
                    plot.setRenderer(3, createLineRenderer(Color.magenta));
                    break;
                case "Rain volume":
                    plot.setDataset(4, getRainVolumes());
                    plot.setRenderer(4, createBarRenderer(Color.blue));
                    secondaryAxis.setVisible(true);
                    break;
                default:
                    break;
            }
        }

        plot.setDomainAxis(new CategoryAxis("Time"));
        plot.setRangeAxis(0, new NumberAxis());
        plot.setRangeAxis(1, secondaryAxis);
        plot.mapDatasetToRangeAxis(4, 1);

        return plot;
    }

    private LineAndShapeRenderer createLineRenderer(Color color) {
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesPaint(0, color);
        return renderer;
    }

    private BarRenderer createBarRenderer(Color color) {
        BarRenderer renderer = new BarRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setSeriesPaint(0, color);
        return renderer;
    }

    private NumberAxis createSecondaryAxis(Color color) {
        NumberAxis axis = new NumberAxis("rain (mm)");
        axis.setLowerBound(0d);
        double maximumVolume = forecastData
                .stream()
                .map(e -> e.rainVolume)
                .reduce(2d, (maxValue, current) -> Math.max(maxValue, current));
        axis.setUpperBound(maximumVolume);
        axis.setLabelPaint(color);
        axis.setTickLabelPaint(color);
        axis.setVisible(false);

        return axis;
    }

    private DefaultCategoryDataset getTemperatures() {
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        forecastData.forEach(entry -> {
            // Only add the data for the selected date
            if (entry.timeStamp.split(" ")[0].equals(selectedDate)) {
                data.addValue(entry.temperature, "temperature (°C)", entry.timeStamp);
            }
        });

        return data;
    }

    private DefaultCategoryDataset getHumidities() {
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        forecastData.forEach(entry -> {
            if (entry.timeStamp.split(" ")[0].equals(selectedDate)) {
                data.addValue(entry.humidity, "humidity (%)", entry.timeStamp);
            }
        });

        return data;
    }

    private DefaultCategoryDataset getWindSpeeds() {
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        forecastData.forEach(entry -> {
            if (entry.timeStamp.split(" ")[0].equals(selectedDate)) {
                data.addValue(entry.windSpeed, "wind speed (m/s)", entry.timeStamp);
            }
        });

        return data;
    }

    private DefaultCategoryDataset getRainProbabilities() {
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        forecastData.forEach(entry -> {
            if (entry.timeStamp.split(" ")[0].equals(selectedDate)) {
                data.addValue(entry.rainProbability, "rain probability (%)", entry.timeStamp);
            }
        });

        return data;
    }

    private DefaultCategoryDataset getRainVolumes() {
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        forecastData.forEach(entry -> {
            if (entry.timeStamp.split(" ")[0].equals(selectedDate)) {
                data.addValue(entry.rainVolume, "rain volume (mm)", entry.timeStamp);
            }
        });

        return data;
    }
}

// <- Own code
