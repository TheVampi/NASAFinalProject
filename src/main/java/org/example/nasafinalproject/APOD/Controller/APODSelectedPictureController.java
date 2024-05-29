package org.example.nasafinalproject.APOD.Controller;

import org.example.nasafinalproject.Database.Dao.*;
import org.example.nasafinalproject.Models.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class APODSelectedPictureController implements Initializable {
    @FXML
    private Label explanationText, dateLabel, titleLabel;
    @FXML private ImageView imageView_TodayPic;

    Picture selectedPicture;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageView_TodayPic.setImage(new Image(selectedPicture.getHdurl()));
        titleLabel.setText("\""+selectedPicture.getTitle()+"\"");
        explanationText.setText(selectedPicture.getExplanation());
        dateLabel.setText(selectedPicture.getDate());
    }

    public void setSelectedPicture(Picture selectedPicture) {
        this.selectedPicture = selectedPicture;
    }
}
