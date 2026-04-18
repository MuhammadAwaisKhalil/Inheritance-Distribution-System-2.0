module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.mail;

    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
    exports org.example.demo.User;
    opens org.example.demo.User to javafx.fxml;
    exports org.example.demo.Property;
    opens org.example.demo.Property to javafx.fxml;
    exports org.example.demo.Login;
    opens org.example.demo.Login to javafx.fxml;
    opens org.example.demo.Inheritors to javafx.fxml;
}