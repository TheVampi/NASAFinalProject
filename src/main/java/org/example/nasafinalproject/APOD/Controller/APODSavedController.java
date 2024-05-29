package org.example.nasafinalproject.APOD.Controller;


import org.example.nasafinalproject.Database.Dao.*;
import org.example.nasafinalproject.Models.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.nasafinalproject.HelloApplication;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class APODSavedController implements Initializable {

    String cssImages = "-fx-border-color: white;\n" +
            "-fx-border-insets: 5;\n" +
            "-fx-border-width: 3;\n" +
            "-fx-border-style: segments(10, 15, 15, 15)  line-cap round;\n" +
            "-fx-border-radius: 25px;\n" +
            "-fx-padding: 15px;\n";

    @FXML TableView<Picture> tablePictures = new TableView();
    List<Picture> pictureList = new ArrayList<>();
    PictureDAO pictureDAO = new PictureDAO();

    String cssCharts = "-fx-border-color: white;\n" +
            "-fx-border-insets: 5;\n" +
            "-fx-border-width: 1.5;\n";


    @FXML private FontIcon backIconBtn, savedIconBtn, queryIconBtn;
    @FXML private HBox topBanner;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        topBanner.setStyle(cssCharts);

        pictureList = pictureDAO.findAll();

        for (Picture pic: pictureList){
            System.out.println(pic);
        }

        tablePictures.setItems(FXCollections.observableArrayList(pictureList));
    }
    @FXML private void onBackBtnClick() throws IOException {
        Stage searchStage = (Stage)backIconBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/APISelector/api-selector-view.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 830, 760);
        searchStage.setTitle("Select an API");
        searchStage.setScene(scene);
        searchStage.show();
    }
    @FXML private void onApiQryBtnClick() throws IOException {
        Stage searchStage = (Stage)backIconBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/APOD/APOD_SearchView.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 830, 760);
        searchStage.setTitle("Search an astronomy picture!");
        searchStage.setScene(scene);
        searchStage.show();
    }
    @FXML private void onHomeBtnClick() throws IOException {
        Stage searchStage = (Stage)backIconBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/APOD/APOD_MainView.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 830, 760);
        searchStage.setTitle("Search an astronomy picture!");
        searchStage.setScene(scene);
        searchStage.show();
    }
    @FXML private void onTableRowClick() throws IOException {

        Picture selectedPic = tablePictures.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/APOD/APOD_SelectedPictureView.fxml"));
        APODSelectedPictureController infoController = new APODSelectedPictureController();
        infoController.setSelectedPicture(selectedPic);

        if (selectedPic==null){
            showAlert(Alert.AlertType.ERROR, "NO PICTURE SELECTED", "Error: No picture selected.",
                    "There´s no information to show because you haven't selected a picture yet.");

        }else {


            loader.setController(infoController);

            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Stage infoStage = new Stage();
            Scene scene = new Scene(root, 727, 660);

            infoStage.setScene(scene);
            infoStage.setTitle("Picture details");
            infoStage.initModality(Modality.APPLICATION_MODAL);
            infoStage.showAndWait();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String message){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(header);
        alert.show();
    }
    @FXML private void onMouseEnteredBtn(){
        Scene scene = queryIconBtn.getScene();
        scene.setCursor(Cursor.HAND);
    }
    @FXML private void onMouseExited(){
        Scene scene = queryIconBtn.getScene();
        scene.setCursor(Cursor.DEFAULT);
    }
}
