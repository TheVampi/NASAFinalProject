package org.example.nasafinalproject.APISelector.Controller;

import com.itextpdf.kernel.colors.Lab;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.nasafinalproject.HelloApplication;

public class APISelectorController implements Initializable {

    @FXML
    private HBox hboxAPOD;
    @FXML
    private HBox hboxMEDIA;
    @FXML
    private HBox hboxMARS;
    @FXML
    private Button btnAtras;
    @FXML
    private Label lblUser;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblUser.setText("User: "+getUsernameFromCSV("user.csv")+"   APIKey: "+getAPIKeyFromCSV("user.csv"));
    }

    @FXML
    public void openAPOD() throws IOException {
        Stage searchStage = (Stage)hboxAPOD.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/APOD/APOD_MainView.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 830, 760);
        searchStage.setTitle("APOD");
        searchStage.setScene(scene);
        searchStage.show();
    }
    @FXML
    public void openMEDIA() throws IOException {
        Stage searchStage = (Stage)hboxMEDIA.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/NASAMedia/nasamedia-view.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 1114, 929);
        searchStage.setTitle("NASA Media Library");
        searchStage.setResizable(false);
        searchStage.setScene(scene);
        searchStage.show();
    }
    @FXML
    public void openMARS() throws IOException {
        Stage searchStage = (Stage)hboxMARS.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/MarsAPI/marsAPI-view.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 1425, 892);
        searchStage.setTitle("MARS Photo API");
        searchStage.setResizable(false);
        searchStage.setScene(scene);
        searchStage.show();
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
        Stage searchStage = (Stage)btnAtras.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/Login/login-view.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 363, 525);
        searchStage.setTitle("Log In");
        searchStage.setScene(scene);
        searchStage.setResizable(false);
        searchStage.show();

    }
}
