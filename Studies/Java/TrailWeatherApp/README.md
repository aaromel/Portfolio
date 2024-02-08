# ThreePlus TrailWeather App

The application has been tested to be working in Tuni linux-desktop using Apache NetBeans IDE and Java 17.0.8.

The main class of the application is App.java (threeplus/trailweather/src/main/java/fi/tuni/trailweather/App.java).
It configures and starts up the JavaFX application. Rest of the processes get taken care by the application itself.

Own code is located in files under trailerweather/src/main/java/fi/tuni/trailweather/:

- GenericUIController.java
- ListViewController.java
- LocationData.java
- MapController.java

## Environment required

Preferrably with these or newer versions to avoid any issues:

- Java 17.0.8
- Maven 3.9.4

Optional: An IDE like Apache NetBeans or IntelliJ Idea to run the application seamlessly.

## Running the application

1. Clone the repository
2. Navigate to the folder "trailweather" on command line
3. Run the project with the following command:

```
mvn clean javafx:run
```

When using NetBeans:

1. Open the project in NetBeans with "Open project" in the "File" menu.
2. Run the project with "Run project" in the "Run" menu.
