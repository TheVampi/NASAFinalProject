package org.example.nasafinalproject.NASAMedia.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.nasafinalproject.HelloApplication;
import org.example.nasafinalproject.Models.MarsRoverImage;
import org.example.nasafinalproject.Models.NasaImage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NASAMediaController implements Initializable {

    @FXML
    private ScrollBar scrollNoImages;
    @FXML
    private Label lblNoImages;
    @FXML
    private TextField txtPrompt;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnRefreshSearch;
    @FXML
    private Button btnClearPrompt;
    @FXML
    private Button btnReturnToAPISelector;
    @FXML
    private GridPane gridImageResults;
    @FXML
    private Label lblUser;
    @FXML
    private Label lblPrompt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setScrollNoImages();
        getCurrentScrollPaneValue();
        setGridPaneDimensions();
        setGridPaneHBoxes();
        scrollNoImages.setValue(1);

        //Seteando el label de informacion del usuario
        lblUser.setText("User: "+getUsernameFromCSV("user.csv")+"   APIKey: "+getAPIKeyFromCSV("user.csv"));
        setlblPrompt("Bienvenido, haz una búsqueda");



    }

    public void setlblPrompt(String prompt) {
        lblPrompt.setText(prompt);
    }

    private void getCurrentScrollPaneValue() {
        //Agregando el listener para obtener el valor del scrollBar
        scrollNoImages.valueProperty().addListener((observable, oldValue, newValue) -> {
            int intValue = newValue.intValue();
            lblNoImages.setText(intValue +" fotos");
        });
    }

    private void setScrollNoImages() {
        scrollNoImages.setMin(1);
        scrollNoImages.setMax(100);
        scrollNoImages.setUnitIncrement(1);
        scrollNoImages.setBlockIncrement(1);
    }

    private void setGridPaneDimensions(){
        // Definir las columnas
        for (int i = 0; i < 5; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(228.8);
            gridImageResults.getColumnConstraints().add(column);
        }
        // Definir las filas
        for (int i = 0; i < 20; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(170);
            gridImageResults.getRowConstraints().add(row);
        }
    }

    private void setGridPaneHBoxes(){
        // Rellenar el GridPane con HBoxes
        int hboxCounter = 1;
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 5; col++) {
                HBox hbox = new HBox();
                hbox.setPrefSize(228.8, 170);
                hbox.setStyle("-fx-alignment: center;"); // Alinear el contenido en el centro
                hbox.setId("imgContainer" + hboxCounter); // Asignar un identificador único
                hbox.setPadding(new Insets(10, 10, 10, 10));
                hboxCounter++;
                // Añadir el HBox a la celda correspondiente del GridPane
                gridImageResults.add(hbox, col, row);
            }
        }
    }

    @FXML
    private void searchAndLoadPrompt(){
        gridImageResults.getChildren().clear();
        setGridPaneDimensions();
        setGridPaneHBoxes();
        String APIURLRequest = "https://images-api.nasa.gov/search?q="+replaceSpaces(txtPrompt.getText())+"&media_type=image&page_size="+ (int) scrollNoImages.getValue();
        System.out.println(APIURLRequest); //Para ver en consola
        setlblPrompt("Se encontraron estos resultados para: "+txtPrompt.getText());
        assignImagesToGridPane(gridImageResults,getterNasaImages(APIURLRequest,txtPrompt.getText()));
    }

    public void assignImagesToGridPane(GridPane gridPane, List<NasaImage> nasaImages) {
        int hboxCounter = 1;
        int imageCounter = 0;
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 5; col++) {
                if (imageCounter < nasaImages.size()) {
                    HBox hbox = (HBox) gridPane.lookup("#imgContainer" + hboxCounter++);
                    NasaImage nasaImage = nasaImages.get(imageCounter++);

                    // Crear ImageView para la imagen
                    ImageView imageView = new ImageView(new Image(nasaImage.getImageUrl()));
                    imageView.setFitWidth(228.8);
                    imageView.setFitHeight(150);
                    imageView.setPreserveRatio(false);


                    // Crear Label para el título
                    Label titleLabel = new Label(nasaImage.getTitle());

                    // Crear VBox para contener la imagen y el título
                    VBox vbox = new VBox(imageView, titleLabel);
                    vbox.setStyle("-fx-alignment: center;");

                    // Añadir el VBox al HBox
                    hbox.getChildren().add(vbox);
                }
            }
        }
    }

    //Debido a que los espacios no estan tolerados en las URL asi es como se representan con %20
    public static String replaceSpaces(String input) {
        if (input == null) {
            return null;
        }
        return input.replace(" ", "%20");
    }

    public List<NasaImage> getterNasaImages(String prompt, String query){
        List<NasaImage> nasaImages = new ArrayList<>();
        try {
            URL url = new URL(prompt);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                JOptionPane.showMessageDialog(null, "No hay resultados para la búsqueda: "+query);
            }
            if (responseCode != 200) {
                JOptionPane.showMessageDialog(null,"HttpResponseCode: " + responseCode+"\nHA OCURRIDO UN ERROR DE API");
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                StringBuilder inline = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(inline.toString());

                JsonNode items = root.path("collection").path("items");
                for (JsonNode item : items) {
                    String title = item.path("data").get(0).path("title").asText();
                    String description = item.path("data").get(0).path("description").asText();
                    String date = item.path("data").get(0).path("date_created").asText();
                    String imageUrl = item.path("links").get(0).path("href").asText();

                    NasaImage nasaImage = new NasaImage(title, description, date, imageUrl);
                    nasaImages.add(nasaImage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nasaImages;
    }

    @FXML
    public void getBack() throws IOException {
        Stage searchStage = (Stage)btnReturnToAPISelector.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/APISelector/api-selector-view.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 830, 760);
        searchStage.setTitle("Select an API");
        searchStage.setScene(scene);
        searchStage.show();
    }

    @FXML
    public void clearEverything(){
        gridImageResults.getChildren().clear();
        setGridPaneDimensions();
        setGridPaneHBoxes();
        txtPrompt.setText("");
        setlblPrompt("Bienvenido, haz una búsqueda");
        scrollNoImages.setValue(1);
    }

    @FXML
    public void refreshSearch(){
        gridImageResults.getChildren().clear();
        setGridPaneDimensions();
        setGridPaneHBoxes();
        setlblPrompt("Se encontraron estos resultados para: "+txtPrompt.getText());
        String APIURLRequest = "https://images-api.nasa.gov/search?q="+replaceSpaces(txtPrompt.getText())+"&media_type=image&page_size="+ (int) scrollNoImages.getValue();
        System.out.println(APIURLRequest); //Para ver en consola

        assignImagesToGridPane(gridImageResults,getterNasaImages(APIURLRequest,txtPrompt.getText()));
    }

    public static String getAPIKeyFromCSV(String filePath) {
        String apiKey = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Saltar la primera línea que es el encabezado
            if ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > 2) {
                    apiKey = values[2];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiKey;
    }

    public static String getUsernameFromCSV(String filePath) {
        String username = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Saltar la primera línea que es el encabezado
            if ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > 0) {
                    username = values[0];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return username;
    }


}
