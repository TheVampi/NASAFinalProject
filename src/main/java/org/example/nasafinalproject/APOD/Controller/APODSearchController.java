package org.example.nasafinalproject.APOD.Controller;

import org.example.nasafinalproject.Database.Dao.*;
import org.example.nasafinalproject.HelloApplication;
import org.example.nasafinalproject.Models.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class APODSearchController implements Initializable {
    String cssImages = "-fx-border-color: white;\n" +
            "-fx-border-insets: 5;\n" +
            "-fx-border-width: 3;\n" +
            "-fx-border-style: segments(10, 15, 15, 15)  line-cap round;\n" +
            "-fx-border-radius: 25px;\n" +
            "-fx-padding: 15px;\n";
    String cssCharts = "-fx-border-color: white;\n" +
            "-fx-border-insets: 5;\n" +
            "-fx-border-width: 1.5;\n";

    @FXML private FontIcon backIconBtn, savedIconBtn, queryIconBtn;
    @FXML private HBox topBanner, image_apodChart;
    @FXML private VBox contentChart;
    @FXML private DatePicker datePicker;
    @FXML private Label explanationText, explanationLabel, dateLabel, titleLabel;
    @FXML private ImageView imageView_TodayPic;
    @FXML private Button storeBtn;

    Picture apod;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        topBanner.setStyle(cssCharts);
        contentChart.setStyle(cssCharts);
        image_apodChart.setStyle(cssImages);

    }
    @FXML private void onSearchBtnClick(){
        LocalDate searchedDate= datePicker.getValue();

        if (searchedDate==null){
            showAlert(Alert.AlertType.ERROR, "NO DATE SELECTED", "Error: No date selected.",
                    "There´s no information to show because you haven't selected a date yet.");
        }
        else{
            String searchedDateStr = searchedDate.toString();

            try {
                apod = makeQuery(searchedDateStr);

                imageView_TodayPic.setImage(new Image(apod.getUrl()));

                titleLabel.setText("\""+apod.getTitle()+"\"");
                explanationText.setText(apod.getExplanation());
                dateLabel.setText(apod.getDate());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            explanationLabel.setVisible(true);
            storeBtn.setVisible(true);
        }
    }
    private Picture makeQuery(String searchedDate) throws Exception {

        String apiKey= "ghuPTPXV9dFB9laeGBdY2laVEJf4BFVgUlcOwber";
        String urlString = "https://api.nasa.gov/planetary/apod?api_key=";
        String jsonString="";

        Picture pic = new Picture();

        URL url = new URL(urlString+apiKey+"&date="+searchedDate+"&thumbs=true");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream stream = connection.getInputStream();
        byte[] arrBytes = stream.readAllBytes();

        for(byte ch : arrBytes){
            jsonString+=(char)ch;
        }
        JSONObject jsonObject = new JSONObject(jsonString);

        //    pictureID      varchar(10) not null
        //                        primary key,
        //    idRequest      int         null,
        //    copyright      int         null,
        //    date           varchar(10) null,
        //    explanation    text        null,
        //    hdurl          text        null,
        //    mediaType      int         null,
        //    serviceVersion varchar(4)  null,
        //    url            text        null,
        //    title          varchar(20) null,
        String copyrightStr, mediaTypeStr;
        MediaType mediaType;
        Copyright copyright;
        RequestAPOD requestAPOD;
        MediaTypeDAO mediaDAO = new MediaTypeDAO();
        CopyrightDAO copyrightDAO = new CopyrightDAO();
        RequestApodDAO requestDAO = new RequestApodDAO();

        pic.setDate((jsonObject.get("date").toString()));
        pic.setPictureID(pic.getDate());
        pic.setTitle(jsonObject.get("title").toString());
        pic.setExplanation(jsonObject.get("explanation").toString());
        pic.setServiceVersion(jsonObject.get("service_version").toString());

        mediaTypeStr= jsonObject.get("media_type").toString();
        mediaType = new MediaType(mediaDAO.getMediaId(mediaTypeStr) ,mediaTypeStr);
        pic.setMediaType(mediaType);
        pic.setMediaTypeID(mediaType.getTypeID());

        try{

            pic.setHdurl(jsonObject.get("hdurl").toString());

        } catch (Exception e){}

        try {
            copyrightStr = jsonObject.get("copyright").toString();
            copyright = new Copyright(copyrightStr);
            pic.setCopyright(copyright);

        } catch (Exception e){}



        if(jsonObject.get("media_type").toString().equals("video"))
            try{pic.setUrl(jsonObject.get("thumbnail_url").toString());} catch (Exception e){}
        else
            try{pic.setUrl(jsonObject.get("url").toString());} catch (Exception e){}

        return pic;
    }
    @FXML private void onStoreBtnClick() {

        boolean hasCopyright = true;
        MediaType mediaType = apod.getMediaType();
        Copyright copyright = apod.getCopyright();
        RequestAPOD requestAPOD = apod.getRequestAPOD();
        PictureDAO pictureDAO = new PictureDAO();
        CopyrightDAO copyrightDAO = new CopyrightDAO();
        RequestApodDAO requestDAO = new RequestApodDAO();


        if (copyright != null) {
            if (!copyrightDAO.save(copyright))
                showAlert(Alert.AlertType.ERROR, "ERROR", "ERROR COPYRIGHT INSERTION", "Oops... something went wrong:(");

            copyright.setCopyrightID(copyrightDAO.getCopyrightId(copyright.getName()));
            apod.setCopyright(copyright);
            apod.setCopyrightID(copyright.getCopyrightID());
        } else {
            hasCopyright = false;
            showAlert(Alert.AlertType.WARNING, "ERROR", "NO COPYRIGHT", "This image has no copyright");
        }
        if (hasCopyright) {
            if (pictureDAO.save(apod))
                showAlert(Alert.AlertType.INFORMATION, "SUCCESS", "IMAGE STORED", "The image has been stored correctly");
            else
                showAlert(Alert.AlertType.WARNING, "ERROR", "ERROR", "Oops... something went wrong:(");
        }
        else {
            if (pictureDAO.saveNoCopyright(apod))
                showAlert(Alert.AlertType.INFORMATION, "SUCCESS", "IMAGE STORED", "The image has been stored correctly");
            else
                showAlert(Alert.AlertType.WARNING, "ERROR", "ERROR", "Oops... something went wrong:(");

        }
    }
    @FXML private void onBackBtnClick(){
        // ABRE VENTANA DEL SELECTOR DE APIS
    }
    @FXML private void onSavedBtnClick() throws IOException {
        Stage searchStage = (Stage)backIconBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/APOD/APOD_SavedView.fxml"));
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