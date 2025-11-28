module project.duckman {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    opens ui.graphical to javafx.fxml;
    opens ui.graphical.controllers to javafx.fxml;
    exports ui.graphical;
}
