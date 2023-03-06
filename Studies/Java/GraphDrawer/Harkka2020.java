package Harkka2020;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
//Tira 2020 harjoitustyön pohja. 
//Muokattu https://www.tutorialspoint.com/javafx/index.htm esimerkeistä.

public class Harkka2020 extends Application {
    Button button1;
  
    Image image;
    WritableImage wImage;
    double width;
    double height;
    ImageView imageView1;
    ImageView imageView2;
    // Own code ->
    int[][] HSEsitys;
    double xKlikkaus;
    double yKlikkaus;
    String txt1;
    String txt2;
    // <-Own code 

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        // Creating an image
        image = new Image(new FileInputStream("Testikuva.jpg"));
        width = image.getWidth();
        height = image.getHeight();
        // Setting the image view 1
        imageView1 = new ImageView(image);

        // Setting the position of the image
        // HUOM! Tämä vaikuttaa hiiren koordinaatteihin kuvassa.
        imageView1.setX(50);
        imageView1.setY(25);

        // setting the fit height and width of the image view
        imageView1.setFitHeight(height / 2);
        imageView1.setFitWidth(width / 2);

        // Setting the preserve ratio of the image view
        imageView1.setPreserveRatio(true);

        // Setting the image view 2
        imageView2 = new ImageView(image);

        // Setting the position of the image
        imageView2.setX(width / 2 + 60);
        imageView2.setY(25);

        // setting the fit height and width of the image view
        imageView2.setFitHeight(height / 2);
        imageView2.setFitWidth(width / 2);

        // Setting the preserve ratio of the image view
        imageView2.setPreserveRatio(true);
        int delta = 50;
        Text text1 = new Text("Anna sallittu intensiteettiero");
        text1.setLayoutX(50);
        text1.setLayoutY(height / 2 + delta);
        TextField textField1 = new TextField();
        textField1.setText("5");
        textField1.setLayoutX(50);
        textField1.setLayoutY(height / 2 + 1.3 * delta);

        Text text2 = new Text("Anna sallittu kokonaisero");
        text2.setLayoutX(50);
        text2.setLayoutY(height / 2 + 2.8 * delta);
        TextField textField2 = new TextField();
        textField2.setText("5");
        textField2.setLayoutX(50);
        textField2.setLayoutY(height / 2 + 3.1 * delta);

        button1 = new Button("Hae yksi komponentti syvyyshaulla");
        button1.setLayoutX(50);
        button1.setLayoutY(height / 2 + 4 * delta);

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) { // Luetaan tekstikenttien tiedot.
                txt1 = textField1.getText();
                txt2 = textField2.getText();
                // Valitaan suoritettava tehtävä.
                if (e.getSource() == button1)
                    Syvyyshaku();
            }
        };

        button1.setOnAction(event);

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                xKlikkaus = e.getX();
                yKlikkaus = e.getY();
                System.out.println("Hiiren klikkaus rivilla " + yKlikkaus + " ja sarakkeella " + xKlikkaus);
                // HUOM! Näkyvä kuvan korkeus ja leveys on puolet varsinaisesta kuvasta.
                // Lisäksi näkyvän kuvan vasen yläreuna on kohdassa(50,25).
                // Kuvassa tarvitaan kokonaislukuja.
            }
        };

        imageView1.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        // Creating a Group object
        Group root = new Group(imageView1, imageView2, text1, textField1,
                text2, textField2, button1);

        // Creating a scene object
        Scene scene = new Scene(root, width + 100, height * 0.85);

        // Setting title to the Stage
        stage.setTitle("Harkka 2020");

        // Adding scene to the stage
        stage.setScene(scene);

        // Displaying the contents of the stage
        stage.show();
    }

    // Kuvan piirtämistä varten.
    public void ManipulateImage() {

        wImage = new WritableImage((int) width, (int) height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter writer = wImage.getPixelWriter();
        Random rand = new Random();
        int rval = rand.nextInt(50);
        rval = rval + 15;
        // Reading through the image.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Retrieving the color of the pixel of the loaded image
                Color color = pixelReader.getColor(x, y);

                // Setting the color to the writable image
                writer.setColor(x, y, color.darker());
                // Pelkkä esimerkki yksittäisen kuvapisteen käsittelyä varten.
                if (Math.abs(x - y) < rval) {
                    int r = (int) Math.round(color.getRed() * 255);
                    int g = (int) Math.round(color.getGreen() * 255);
                    int b = (int) Math.round(color.getBlue() * 255);
                    int grayscale = (int) Math.round(0.3 * r + 0.59 * g + 0.11 * b);
                    System.out.println("Harmaasavy arvo kuvapisteelle on " + grayscale);
                    writer.setColor(x, y, color.GREEN);
                }
            }
        }
        imageView2.setImage(wImage);

    }

    // Own code ->

    // Harmaasävyesityksen luomista varten.
    public void HarmaasavyEsitys() {
        PixelReader pixelReader = image.getPixelReader();
        // Koko määräytyy kuvan mittojen mukaan.
        HSEsitys = new int[1080][1080];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                // Määritetään pisteen harmaasävyarvo.
                Color color = pixelReader.getColor(x, y);
                int r = (int) Math.round(color.getRed() * 255);
                int g = (int) Math.round(color.getGreen() * 255);
                int b = (int) Math.round(color.getBlue() * 255);
                int grayscale = (int) Math.round(0.3 * r + 0.59 * g + 0.11 * b);

                // Lisätään arvo harmaasävyesitystaulukkoon.
                HSEsitys[y][x] = grayscale;
            }
        }
    }

    public void Syvyyshaku() {

        // Luodaan harmaasävyesitys.
        HarmaasavyEsitys();

        // Kuvaa varten.
        wImage = new WritableImage((int) width, (int) height);
        PixelWriter writer = wImage.getPixelWriter();

        // Luodaan graafi. Solmuja on enimmillään 1080*1080, joten sijoitetaan 1200000.
        WeightedGraph.Graafi graafi = new WeightedGraph.Graafi(1200000);

        // Hiiren painalluksen koordinaatit muunnettuna.
        Double yKlik = (yKlikkaus - 25) * 2;
        Double xKlik = (xKlikkaus - 50) * 2;
        int rivi = (int) Math.round(yKlik);
        int sarake = (int) Math.round(xKlik);

        // Alustetaan muuttujat intensiteettieroa ja kokonaiseroa varten.
        int iEro = 0;
        int kokEro = 0;

        // Mikäli arvoja ei anneta, erojen arvoksi riittävän suuri arvo, joka
        // myöhemmät vertailut menevät aina läpi.
        if (txt1.equals("")) {
            iEro = 1000000;
        } else {
            iEro = Integer.parseInt(txt1);
        }
        if (txt2.equals("")) {
            kokEro = 1000000;
        } else {
            kokEro = Integer.parseInt(txt2);
        }

        // Luodaan taulukko, joka toimii apuna syvyyshaussa.
        boolean[][] loydetty = new boolean[1080][1080];

        // Luodaan pino syvyyshakua varten.
        DynArrStack pino = new DynArrStack();

        // Sijoitetaan pinon päälle rivin ja sarakkeen arvo.
        pino.push(rivi + "," + sarake);

        // Alustetaan muuttujat myöhempää varten.
        int vanhaRivi = rivi;
        int vanhaSarake = sarake;

        // Syvyyshaku lopetetaan kun pinossa ei ole enää käsiteltäviä alkioita.
        while (pino.isEmpty() == false) {

            // Poistetaan pinon huipulta alkio, ja muutetaan se merkkijonoksi.
            String paikka = pino.pop().toString();

            // Merkkijonosta saadaan koordinaatit muuntamalla merkkijonon osat.
            int rivi2 = Integer.parseInt(paikka.split(",")[0]);
            int sarake2 = Integer.parseInt(paikka.split(",")[1]);

            // Tarkastetaan, että koordinaattien kohdassa ei ole vielä käyty ja että
            // ne eivät sisällä arvoja, jotka ylittävät taulukon rajat. Jos tämä ei
            // toteudu. Siirrytään while-lausekkeen seuraavaan iteraatioon.
            if (rivi2 < 0 || sarake2 < 0 || rivi2 >= 1080 || sarake2 >= 1080 || loydetty[rivi2][sarake2]) {
                continue;
            }

            // Merkataan kohta löydetyksi.
            loydetty[rivi2][sarake2] = true;

            // Lisätään koordinaatteihin perustuva kaari graafiin, joka sisältää
            // harmaasävyarvot
            // sekä intensiteettieron. vanhaRivi- ja vanhaSarake-muuttujat pohjautuvat
            // edellisiin
            // koordinaatteihin.
            graafi.lisääKaari(HSEsitys[vanhaRivi][vanhaSarake], HSEsitys[rivi2][sarake2],
                    Math.abs(HSEsitys[vanhaRivi][vanhaSarake] - HSEsitys[rivi2][sarake2]));

            // Piirretään piste kuvaan.
            writer.setColor(sarake2, rivi2, Color.BLACK);

            // Muuttujat edellisille koordinaateille.
            vanhaRivi = rivi2;
            vanhaSarake = sarake2;

            // Tarkastetaan, että koordinaatit eivät ole taulukon reunoilla.
            if (rivi2 > 0 && sarake2 > 0 && rivi2 < 1079 && sarake2 < 1079) {

                // Tarkastetaan jokainen pisteen ympärillä (8 pistettä) oleva piste. Mikäli
                // viereisen pisteen intensiteettiero ja kokonaisero eivät ylity, lisätään
                // viereinen
                // piste pinoon käsiteltäväksi.
                if (Math.abs(HSEsitys[rivi2][sarake2] - HSEsitys[rivi2 - 1][sarake2 - 1]) <= iEro &&
                        Math.abs(HSEsitys[rivi2 - 1][sarake2 - 1] - HSEsitys[rivi][sarake]) <= kokEro) {
                    pino.push((rivi2 - 1) + "," + (sarake2 - 1));
                }
                if (Math.abs(HSEsitys[rivi2][sarake2] - HSEsitys[rivi2 - 1][sarake2]) <= iEro &&
                        Math.abs(HSEsitys[rivi2 - 1][sarake2] - HSEsitys[rivi][sarake]) <= kokEro) {
                    pino.push((rivi2 - 1) + "," + sarake2);
                }
                if (Math.abs(HSEsitys[rivi2][sarake2] - HSEsitys[rivi2 - 1][sarake2 + 1]) <= iEro &&
                        Math.abs(HSEsitys[rivi2 - 1][sarake2 + 1] - HSEsitys[rivi][sarake]) <= kokEro) {
                    pino.push((rivi2 - 1) + "," + (sarake2 + 1));
                }
                if (Math.abs(HSEsitys[rivi2][sarake2] - HSEsitys[rivi2][sarake2 + 1]) <= iEro &&
                        Math.abs(HSEsitys[rivi2][sarake2 + 1] - HSEsitys[rivi][sarake]) <= kokEro) {
                    pino.push(rivi2 + "," + (sarake2 + 1));
                }
                if (Math.abs(HSEsitys[rivi2][sarake2] - HSEsitys[rivi2][sarake2 - 1]) <= iEro &&
                        Math.abs(HSEsitys[rivi2][sarake2 - 1] - HSEsitys[rivi][sarake]) <= kokEro) {
                    pino.push(rivi2 + "," + (sarake2 - 1));
                }
                if (Math.abs(HSEsitys[rivi2][sarake2] - HSEsitys[rivi2 + 1][sarake2 - 1]) <= iEro &&
                        Math.abs(HSEsitys[rivi2 + 1][sarake2 - 1] - HSEsitys[rivi][sarake]) <= kokEro) {
                    pino.push((rivi2 + 1) + "," + (sarake2 - 1));
                }
                if (Math.abs(HSEsitys[rivi2][sarake2] - HSEsitys[rivi2 + 1][sarake2]) <= iEro &&
                        Math.abs(HSEsitys[rivi2 + 1][sarake2] - HSEsitys[rivi][sarake]) <= kokEro) {
                    pino.push((rivi2 + 1) + "," + sarake2);
                }
                if (Math.abs(HSEsitys[rivi2][sarake2] - HSEsitys[rivi2 + 1][sarake2 + 1]) <= iEro &&
                        Math.abs(HSEsitys[rivi2 + 1][sarake2 + 1] - HSEsitys[rivi][sarake]) <= kokEro) {
                    pino.push((rivi2 + 1) + "," + (sarake2 + 1));
                }
            }
        }
        // Asetetaan kuva.
        imageView2.setImage(wImage);
    }

    // <- Own code

    public static void main(String args[]) {
        launch(args);
    }
}