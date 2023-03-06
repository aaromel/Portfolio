This project is a program that creates a graph of grayscale representation of a picture and draws an image of one component of the graph based on defined constricts and a point selected by the user. The component is formed using dept-first search.

User selects the point of the image by clicking it with mouse. User defines restrictions for the forming the graph by defining the allowed intensity value difference between points next to each other (intensiteettiero) and by defining the allowed total difference of values between the clicked point and every other point (kokonaisero). The values of points are based on their grayscale values. 

Demo1 picture shows the result when the ocean in the picture is clicked and when the intensity value difference is set to 5 but total difference is not set at all. Demo2 picture shows the result when the total difference is set to 25.  

The FXM-files should be ignored. Own code is located in files:
DynArrStack.java
Harkka2020.java
WeightedGraph.java

The GUI has not been implemented by me. 
