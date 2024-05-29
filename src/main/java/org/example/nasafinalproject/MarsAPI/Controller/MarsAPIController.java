package org.example.nasafinalproject.MarsAPI.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.example.nasafinalproject.Database.Dao.CamerasListDao;
import org.example.nasafinalproject.HelloApplication;
import org.example.nasafinalproject.Models.MarsRoverImage;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;


import javafx.scene.control.*;
import org.example.nasafinalproject.Models.RoverCameras;

import java.io.IOException;

public class MarsAPIController implements Initializable {

    @FXML
    private GridPane gridMarsImages;
    @FXML
    private RadioButton radioBtnCuriosity;
    @FXML
    private RadioButton radioBtnPerseverance;
    @FXML
    private RadioButton radioBtnOpportunity;
    @FXML
    private RadioButton radioBtnSpirit;
    @FXML
    private DatePicker datePickerEarth;
    @FXML
    private ComboBox<RoverCameras> comboCameras;
    @FXML
    private Spinner<Integer> spinnerSolDay;
    @FXML
    private Label lblUserAndAPI;
    @FXML
    private Button btnFilterByMarsDays;
    @FXML
    private Button btnFilter;
    @FXML
    private Button btnLogOut;
    String [] arrayURLS;

    CamerasListDao camerasListDao = new CamerasListDao();

    private String radioButtonRover, formattedDate, marDay, selectedIdCamera;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setGridPaneDimensions();
        setGridPaneHBoxes();
        setRadioButtonValues(radioBtnPerseverance,radioBtnCuriosity,radioBtnOpportunity,radioBtnSpirit);

        // Optional: Grouping RadioButtons to make them mutually exclusive
        ToggleGroup group = new ToggleGroup();
        radioBtnPerseverance.setToggleGroup(group);
        radioBtnCuriosity.setToggleGroup(group);
        radioBtnOpportunity.setToggleGroup(group);
        radioBtnSpirit.setToggleGroup(group);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue != null) {
                    RadioButton selectedRadioButton = (RadioButton) newValue;
                    radioButtonRover = selectedRadioButton.getText();
                    updateCameraComboBox(radioButtonRover);
                }
            }
        });

        // Configurar el StringConverter para el ComboBox
        comboCameras.setConverter(new StringConverter<RoverCameras>() {
            @Override
            public String toString(RoverCameras roverCameras) {
                return roverCameras != null ? roverCameras.getDescription() : "";
            }

            @Override
            public RoverCameras fromString(String string) {
                return comboCameras.getItems().stream().filter(ap ->
                        ap.getDescription().equals(string)).findFirst().orElse(null);
            }
        });

        // Add listener to update the selectedCameraId when the selection changes
        comboCameras.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedIdCamera = newValue.getIdCamera();
                System.out.println("Selected Camera ID: " + selectedIdCamera); // For debugging
            }
        });

        // Add a ChangeListener to update the selected date automatically
        datePickerEarth.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                formattedDate = newValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                System.out.println("Selected date: " + formattedDate);
            }
        });

        // Initialize the Spinner with a value factory
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2000, 1,1);
        spinnerSolDay.setValueFactory(valueFactory);

        // Add a ChangeListener to update the spinner value automatically
        spinnerSolDay.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if (newValue != null) {
                    marDay = newValue.toString();
                    System.out.println("Spinner value: " + marDay);
                }
            }
        });

        //Seteando el label de informacion del usuario
        lblUserAndAPI.setText("User: "+getUsernameFromCSV("user.csv")+"   APIKey: "+getAPIKeyFromCSV("user.csv"));
    }

    private void updateCameraComboBox(String roverName) {
        // Fetch the camera descriptions from the DAO based on the selected rover name
        List<RoverCameras> camerasInfo = camerasListDao.getRoverCamerasInfo(roverName);
        ObservableList<RoverCameras> observableCamerasInfo = FXCollections.observableArrayList(camerasInfo);

        // Update the ComboBox with the new list of camera descriptions
        comboCameras.setItems(observableCamerasInfo);
    }

    @FXML
    private void searchAndLoadImages(){
        gridMarsImages.getChildren().clear();
        setGridPaneDimensions();
        setGridPaneHBoxes();
        System.out.println("MarsRoverImages: " + urlSetterByEarthDays(getAPIKeyFromCSV("user.csv"),radioButtonRover,selectedIdCamera,formattedDate));

        List<MarsRoverImage> marsRoverImages = fetchMarsRoverImages(urlSetterByEarthDays(getAPIKeyFromCSV("user.csv"),radioButtonRover,selectedIdCamera,formattedDate));
        assignImagesToGridPane(gridMarsImages, marsRoverImages);

    }

    @FXML
    private void searchAndLoadImagesByMarsDays(){
        gridMarsImages.getChildren().clear();
        setGridPaneDimensions();
        setGridPaneHBoxes();
        System.out.println("MarsRoverImages: " + urlSetterByMarsDays(getAPIKeyFromCSV("user.csv"),radioButtonRover,selectedIdCamera,marDay));
        List<MarsRoverImage> marsRoverImages = fetchMarsRoverImages(urlSetterByMarsDays(getAPIKeyFromCSV("user.csv"),radioButtonRover,selectedIdCamera,marDay));
        assignImagesToGridPane(gridMarsImages, marsRoverImages);

    }



    private void setGridPaneDimensions(){
        // Definir las columnas
        for (int i = 0; i < 5; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(208.4);
            gridMarsImages.getColumnConstraints().add(column);
        }
        // Definir las filas
        for (int i = 0; i < 20; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(170);
            gridMarsImages.getRowConstraints().add(row);
        }
    }

    private void setGridPaneHBoxes(){
        // Rellenar el GridPane con HBoxes
        int hboxCounter = 1;
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 5; col++) {
                HBox hbox = new HBox();
                hbox.setPrefSize(208.4, 170);
                hbox.setStyle("-fx-alignment: center;"); // Alinear el contenido en el centro
                hbox.setId("imgContainer" + hboxCounter); // Asignar un identificador único
                hbox.setPadding(new Insets(10, 10, 10, 10));
                hboxCounter++;
                // Añadir el HBox a la celda correspondiente del GridPane
                gridMarsImages.add(hbox, col, row);
            }
        }
    }

    @FXML
    private void handleGetDate() {
        formattedDate = getFormattedDate(datePickerEarth);
        System.out.println("Selected date: " + formattedDate);
    }

    private String getFormattedDate(DatePicker datePicker) {
        LocalDate date = datePicker.getValue();
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return date.format(formatter);
        } else {
            return null;
        }
    }

    public void setRadioButtonValues(RadioButton rb1, RadioButton rb2, RadioButton rb3, RadioButton rb4) {
        rb1.setText("Perseverance");
        rb1.setUserData("perseverance");

        rb2.setText("Curiosity");
        rb2.setUserData("curiosity");

        rb3.setText("Opportunity");
        rb3.setUserData("opportunity");

        rb4.setText("Spirit");
        rb4.setUserData("spirit");
    }

    public List<MarsRoverImage> fetchMarsRoverImages(String finalURL) {
        List<MarsRoverImage> marsRoverImages = new ArrayList<>();

        try {
//            URL url = new URL("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?api_key=DEMO_KEY&sol=1000&camera=fhaz");
            URL url = new URL(finalURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
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

                JsonNode items = root.path("photos");
                arrayURLS = new String[items.size()];
                int indexArray = 0;
                for (JsonNode item : items) {
                    String imageUrl = item.path("img_src").asText();

                    //Para depurar en consola
                    System.out.println(imageUrl);
                    //Agregando las URLS a un arreglo
                    arrayURLS[indexArray] = imageUrl;
                    indexArray++;
                    String earthDate = item.path("earth_date").asText();
                    String roverNameResult = item.path("rover").path("name").asText();

                    MarsRoverImage marsRoverImage = new MarsRoverImage(imageUrl, earthDate, roverNameResult);

                    marsRoverImages.add(marsRoverImage);
                }
                for(int i=0; i<arrayURLS.length; i++){
                    System.out.println("URL Numero: "+i+" Contenido: "+arrayURLS[i]);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return marsRoverImages;
    }

    public String urlSetterByEarthDays(String APIKey, String rover, String camera, String earthDate){
        String url = "https://api.nasa.gov/mars-photos/api/v1/rovers/"+rover+"/photos?api_key="+APIKey+"&earth_date="+earthDate+"&camera="+camera;
        return url;
    }
    public String urlSetterByMarsDays(String APIKey, String rover, String camera, String marDay){
        String url = "https://api.nasa.gov/mars-photos/api/v1/rovers/"+rover+"/photos?api_key="+APIKey+"&sol="+marDay+"&camera="+camera;
        return url;
    }



    public void assignImagesToGridPane(GridPane gridPane, List<MarsRoverImage> marsRoverImages) {

        int hboxCounter = 1;
        int imageCounter = 0;
        int URLIndex = 0;
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 5; col++) {
                if (imageCounter < marsRoverImages.size()) {
                    HBox hbox = (HBox) gridPane.lookup("#imgContainer" + hboxCounter++);
                    MarsRoverImage marsRoverImage = marsRoverImages.get(imageCounter++);

                    ImageView imageView = new ImageView(new Image("http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01000/opgs/edr/fcam/FRB_486265257EDR_F0481570FHAZ00323M_.JPG"));
//                    ImageView imageView = new ImageView();
                    String imageUrl = marsRoverImage.getImageUrl();
                    String url = arrayURLS[URLIndex];
                    URLIndex++;
//                    String repairedURL = marsRoverImage.getImageUrl().replaceFirst("http","https");
//                    repairedURL = repairedURL.replaceFirst("https","https");
//                    Image image = new Image(repairedURL);
                    Image image = new Image(marsRoverImage.getImageUrl());
                    imageView.setImage(image);
                    imageView.setFitWidth(208.4);
                    imageView.setFitHeight(150);
                    imageView.setPreserveRatio(false);
                    imageView.setVisible(true);

//                    imageView.setFitWidth(228.8);
//                    imageView.setFitHeight(150);
//                    imageView.setPreserveRatio(false);

                    Label date = new Label(marsRoverImage.getEarthDate());
                    //Label url = new Label(marsRoverImage.getImageUrl());
                    // Crear VBox para contener la imagen y el título
                    VBox vbox = new VBox(imageView, date);

                    vbox.setStyle("-fx-alignment: center;");

                    // Añadir el VBox al HBox
                    hbox.getChildren().add(vbox);

                }
            }
        }
    }

    private void clearGridPane(GridPane gridPane) {
        gridPane.getChildren().clear();
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

    @FXML
    public void getBack() throws IOException {
        Stage searchStage = (Stage)btnLogOut.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/Login/login-view.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 363, 525);
        searchStage.setTitle("Log In");
        searchStage.setScene(scene);
        searchStage.setResizable(false);
        searchStage.show();

    }

}
