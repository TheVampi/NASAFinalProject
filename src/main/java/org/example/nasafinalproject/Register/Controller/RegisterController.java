package org.example.nasafinalproject.Register.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.nasafinalproject.Database.Dao.UsersDao;
import org.example.nasafinalproject.HelloApplication;
import org.example.nasafinalproject.Models.Users;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private TextField txtUserRegister;
    @FXML
    private TextField txtName;
    @FXML
    private PasswordField txtPasswordRegister;
    @FXML
    private PasswordField txtPasswordRepeat;
    @FXML
    private TextField txtAPIKey;
    @FXML
    private Label lblGetAnAPI;
    @FXML
    private ToggleButton toggleAdmin;
    @FXML
    private Button btnRegister;



    UsersDao usersDao = new UsersDao();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblGetAnAPI.setOnMouseClicked(event -> openWebpage("https://api.nasa.gov"));
    }

    @FXML
    protected void onRegisterButtonClick() throws IOException {



        if ((txtPasswordRegister.getText()).equals((txtPasswordRepeat.getText()))){
            Users users = new Users();
            users.setUsername(txtUserRegister.getText());
            users.setPassword(txtPasswordRegister.getText());
            users.setAPIKey(txtAPIKey.getText());
            users.setIsAdmin(onAdminToggleClick());
            usersDao.save(users);

            Stage searchStage = (Stage)btnRegister.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/Login/login-view.fxml"));
            Scene scene = new Scene((Parent)fxmlLoader.load(), 363, 525);
            searchStage.setTitle("Log In");
            searchStage.setScene(scene);
            searchStage.setResizable(false);
            searchStage.show();
        } else {
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden, favor de corregir");
            txtPasswordRegister.clear();
            txtPasswordRepeat.clear();
        }

    }

    public void openWebpage(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected int onAdminToggleClick() {
        if (toggleAdmin.isSelected()) {
            toggleAdmin.setText("       ✓");
            return 1;
        }else {
            toggleAdmin.setText(" ✖       ");
            return 0;
        }
    }

}
