package org.example.nasafinalproject.Login.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.nasafinalproject.Database.Dao.UsersDao;

import java.io.FileWriter;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.nasafinalproject.HelloApplication;
import org.example.nasafinalproject.Models.Users;

import javax.swing.*;
import java.io.IOException;

public class LoginController implements Initializable {

    @FXML
    private Button btnLogin;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtUser;
    @FXML
    private Label lblRegisterView;
    @FXML
    private Button btnGuest;

    UsersDao usersDao = new UsersDao();
    Users user = new Users();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblRegisterView.setOnMouseClicked(this::handleLabelClick);

    }

    private void handleLabelClick(javafx.scene.input.MouseEvent mouseEvent) {

        Stage stageActual = (Stage) lblRegisterView.getScene().getWindow();

        stageActual.close();

        try {
            // Cargar el FXML del nuevo Stage
            //FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("ClientesStage.fxml"));
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("views/Register/register-view.fxml"));
            Parent root = loader.load();

            // Crear un nuevo Stage
            Stage stageRegister = new Stage();
            stageRegister.setTitle("Registrar usuario");
            stageRegister.setScene(new Scene(root, 395, 525));
            stageRegister.setResizable(false);

            // Mostrar el nuevo Stage
            stageRegister.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void loginAction() throws IOException {
        if(usersDao.authenticate(txtUser.getText(), txtPassword.getText())){
            if(usersDao.isAdmin(txtUser.getText(), txtPassword.getText())){
                JOptionPane.showMessageDialog(null, "Usuario administrador detectado");
                //Creando archivo con las configuraciones del inicio de sesion:
                saveUserInfoToCSV(usersDao.getUserInfoInCSV(txtUser.getText(), txtPassword.getText()),"user.csv");

                Stage searchStage = (Stage)btnLogin.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/AdminDashboard/admindashboard-view.fxml"));
                Scene scene = new Scene((Parent)fxmlLoader.load(), 830, 760);
                searchStage.setTitle("Modify Users");
                searchStage.setScene(scene);
                searchStage.show();
            }else {
                //Creando archivo con las configuraciones del inicio de sesion:
                saveUserInfoToCSV(usersDao.getUserInfoInCSV(txtUser.getText(), txtPassword.getText()),"user.csv");

                Stage searchStage = (Stage)btnLogin.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/APISelector/api-selector-view.fxml"));
                Scene scene = new Scene((Parent)fxmlLoader.load(), 830, 760);
                searchStage.setTitle("Select an API");
                searchStage.setScene(scene);
                searchStage.show();
            }

        }else {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrecta");
            txtUser.clear();
            txtPassword.clear();
        }

    }

    public static void saveUserInfoToCSV(String userInfo, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("username,password,APIKey,isAdmin\n");
            writer.append(userInfo);
            writer.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean userExists(TextField txtUser) {
        boolean isUserExists = usersDao.doesUserExist(txtUser.getText());
        return isUserExists;
    }

    public void guestAction() throws IOException{
        //Creando archivo con las configuraciones del inicio de sesion:
        try (FileWriter writer = new FileWriter("user.csv")) {
            writer.append("username,password,APIKey,isAdmin\n");
            writer.append("GUEST_USER,DEFAULT_PASSWORD,YCD1nezNKOp7hDnaNLcfgHuxEi3HeEgHakcYC3jA,0");
            writer.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage searchStage = (Stage)btnLogin.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/APISelector/api-selector-view.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 830, 760);
        searchStage.setTitle("Select an API");
        searchStage.setScene(scene);
        searchStage.show();
    }
}