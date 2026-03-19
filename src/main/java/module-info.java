module project.duckman {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;
    opens graphical to javafx.fxml, javafx.graphics;
    opens graphical.controllers to javafx.fxml;
}

