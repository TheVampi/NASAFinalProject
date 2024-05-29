package org.example.nasafinalproject.AdminDashboard.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.nasafinalproject.Database.Dao.UsersDao;
import org.example.nasafinalproject.HelloApplication;
import org.example.nasafinalproject.Models.Users;
import org.example.nasafinalproject.UpdateUser.Controller.UpdateUser;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {
    @FXML
    public TableView<Users> tblUsers;
    @FXML
    public Button btnLogOut;
    @FXML
    public Button btnEditUser;
    @FXML
    public Button btnDeleteUser;

    UsersDao usersDao = new UsersDao();
    public List<Users> usersList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Llenando la tableview de users
        usersList = usersDao.findAll();
        tblUsers.setItems(FXCollections.observableArrayList(usersList));
    }

    @FXML
    public void logOut(){

    }

    @FXML
    public void updateSelectedUser() {

        if (tblUsers.getSelectionModel().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleccione un usuario primero!!");
        } else {

            Stage updateStage = new Stage();
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("views/UpdateUser/updateuser-view.fxml"));

            Users usersTBLUpdate = tblUsers.getSelectionModel().getSelectedItem();
            UpdateUser updateController = new UpdateUser();
            updateController.setUsers(usersTBLUpdate);
            loader.setController(updateController);
            try{
                Parent root = loader.load();
                Scene scene = new Scene(root, 400, 400);
                updateStage.setScene(scene);
                updateStage.setTitle("Modificar User");
                updateStage.initModality(Modality.APPLICATION_MODAL);
                updateStage.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void deleteSelectedUser(){
        if (tblUsers.getSelectionModel().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleccione un usuario primero!!");
        } else {
            //Obteniendo la fila seleccionada
            Users usersTBL = tblUsers.getSelectionModel().getSelectedItem();
            UsersDao usersDaoTBL = new UsersDao();
            if(usersTBL != null){
                if(usersDaoTBL.deleteUser(usersTBL.getUsername())){
                    JOptionPane.showMessageDialog(null, "El usuario fue eliminado");
                }else{
                    JOptionPane.showMessageDialog(null, "No se pudo borrar el usuario");
                }
            }
            refreshData();
        }


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

    @FXML
    public void refreshData(){
        //Llenando la tableview
        usersList = usersDao.findAll();
        tblUsers.setItems(FXCollections.observableArrayList(usersList));
    }


}
