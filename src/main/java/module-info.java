module org.example.nasafinalproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.nasafinalproject to javafx.fxml;
    exports org.example.nasafinalproject;
    exports org.example.nasafinalproject.Login;
    opens org.example.nasafinalproject.Login to javafx.fxml;
}