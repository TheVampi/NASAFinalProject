package org.example.nasafinalproject.APOD.Controller;


import org.example.nasafinalproject.ApachePOIReports.PDFReportGenerator;
import org.example.nasafinalproject.HelloApplication;
import org.example.nasafinalproject.Models.*;
import org.example.nasafinalproject.Database.Dao.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.kordamp.ikonli.javafx.FontIcon;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.ikonli.javafx.FontIcon;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;

public class APOD_Controller implements Initializable {

    String cssImages = "-fx-border-color: white;\n" +
            "-fx-border-insets: 5;\n" +
            "-fx-border-width: 3;\n" +
            "-fx-border-style: segments(10, 15, 15, 15)  line-cap round;\n" +
            "-fx-border-radius: 25px;\n" +
            "-fx-padding: 15px;\n";
    String cssCharts = "-fx-border-color: white;\n" +
            "-fx-border-insets: 5;\n" +
            "-fx-border-width: 1.5;\n";

    @FXML private Label explanationText, dateLabel, infoText, titleLabel;
    @FXML private HBox topBanner, infoChart, image_apodChart;
    @FXML private VBox contentChart, mainLayout;
    @FXML private ImageView imageView_TodayPic, bannerImage, sampleImage;
    @FXML private FontIcon backIconBtn, savedIconBtn, queryIconBtn;

    private Picture apod;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        topBanner.setStyle(cssCharts);
        infoChart.setStyle(cssCharts);
        contentChart.setStyle(cssCharts);
        image_apodChart.setStyle(cssImages);

        try {
            apod = makeQuery();

            System.out.println(apod.getUrl());
//            imageView_TodayPic.setImage(new Image(apod.getHdurl()));
            imageView_TodayPic.setImage(new Image(apod.getUrl()));
            titleLabel.setText("\""+apod.getTitle()+"\"");
            explanationText.setText(apod.getExplanation());
            dateLabel.setText(apod.getDate());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
    private Picture makeQuery() throws Exception {

        String apiKey= "UlZNnPtlUMRlFxj3b1tWURXVaYIBgdw031OOhQl9";
        String urlString = "https://api.nasa.gov/planetary/apod?api_key=";
        String jsonString="";

        Picture pic = new Picture();

        URL url = new URL(urlString+apiKey+"&thumbs=true");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream stream = connection.getInputStream();
        byte[] arrBytes = stream.readAllBytes();

        for(byte ch : arrBytes){
            jsonString+=(char)ch;
        }
        JSONObject jsonObject = new JSONObject(jsonString);

        //date, title, explanation, hdurl, media_type, url
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

        try{pic.setHdurl(jsonObject.get("hdurl").toString());} catch (Exception e){}

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
    @FXML private void onStoreBtnClick(){

        boolean hasCopyright = true;

        Copyright copyright = apod.getCopyright();

        PictureDAO pictureDAO = new PictureDAO();
        CopyrightDAO copyrightDAO = new CopyrightDAO();



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
    @FXML private void onBackBtnClick() throws IOException {
        Stage searchStage = (Stage)backIconBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/APISelector/api-selector-view.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 830, 760);
        searchStage.setTitle("Select an API");
        searchStage.setScene(scene);
        searchStage.show();
        //ABRE PANTALLA DEL SELECTOR DE APIS

    }
    @FXML private void onSavedBtnClick() throws IOException {
        Stage searchStage = (Stage)backIconBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/APOD/APOD_SavedView.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 830, 760);
        searchStage.setTitle("Search an astronomy picture!");
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
    @FXML private void onReportBtnClick() throws IOException{
        String reportPath= "GeneratedReports/weekly_report.pdf";
//        String reportPath= "weekly_report.pdf";
        File file = new File(reportPath);
        file.getParentFile().mkdirs();
        new PDFReportGenerator().createPdf(reportPath);
        openFile(reportPath);
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

    private void openFile(String filename)  {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(filename);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
    }

}