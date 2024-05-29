package org.example.nasafinalproject.UpdateUser.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.nasafinalproject.Database.Dao.UsersDao;
import org.example.nasafinalproject.Models.Users;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateUser implements Initializable {
    @FXML
    private TextField txtusername;
    @FXML
    private TextField txtpassword;
    @FXML
    private TextField txtAPIKey;
    @FXML
    private TextField txtisAdmin;
    @FXML
    private Button btnUpdateUser;
    @FXML
    private Button btnCancel;

    Users user = new Users();
    UsersDao usersDao = new UsersDao();

    String username;
    String password;
    String APIKey;
    int isAdmin;

    public UpdateUser() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtusername.setText(username);
        txtpassword.setText(password);
        txtAPIKey.setText(APIKey);
        txtisAdmin.setText(String.valueOf(isAdmin));
    }

    @FXML
    private void cancel(){
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void setUsers(Users userParam) {
        this.user = userParam;
        username = user.getUsername();
        password = user.getPassword();
        APIKey = user.getAPIKey();
        isAdmin = user.getIsAdmin();
    }

    @FXML
    private void updateUser(){
        Users user = new Users();
        username = txtusername.getText();
        password = txtpassword.getText();
        APIKey = txtAPIKey.getText();
        isAdmin = Integer.parseInt(txtisAdmin.getText());
        user.setUsername(username);
        user.setPassword(password);
        user.setAPIKey(APIKey);
        user.setIsAdmin(isAdmin);
        if(usersDao.update(user)){
            JOptionPane.showMessageDialog(null, "Usuario actualizado exitosamente!");
        }else {
            JOptionPane.showMessageDialog(null, "Error al actualizar el usuario");
        }
        Stage stage = (Stage) btnUpdateUser.getScene().getWindow();
        stage.close();
    }

}
