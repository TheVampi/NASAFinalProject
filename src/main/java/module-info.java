module org.example.nasafinalproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires layout;
    requires kernel;
    requires io;
    requires org.json;


    requires org.apache.poi.ooxml;

    requires org.slf4j;
    requires org.apache.logging.log4j;

    exports org.example.nasafinalproject;
    exports org.example.nasafinalproject.Login.Controller;

    //Los siguientes son los opens para poder enlazar los controladores con los fxml
    opens org.example.nasafinalproject.Login.Controller to javafx.fxml;
    opens org.example.nasafinalproject.MarsAPI.Controller to javafx.fxml;
    opens org.example.nasafinalproject.AdminDashboard.Controller to javafx.fxml;
    opens org.example.nasafinalproject.APISelector.Controller to javafx.fxml;
    opens org.example.nasafinalproject.APOD.Controller to javafx.fxml;
    opens org.example.nasafinalproject.NASAMedia.Controller to javafx.fxml;
    opens org.example.nasafinalproject.Register.Controller to javafx.fxml;
    opens org.example.nasafinalproject.UpdateUser.Controller to javafx.fxml;

    opens org.example.nasafinalproject.Models;


}