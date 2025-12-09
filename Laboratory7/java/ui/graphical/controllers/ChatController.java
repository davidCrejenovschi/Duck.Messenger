package ui.graphical.controllers;

import entities.massages.Message;
import entities.users.abstracts.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import services.users.DuckService;
import services.users.PersonService;
import ui.graphical.ScreenManager;

public class ChatController {

    private User currentUser;

    @FXML
    private TextField toField;
    @FXML
    private TextField replyToField;
    @FXML
    private TextArea messageField;
    @FXML
    private Button sendButton;
    @FXML
    private Label userLabel;
    @FXML
    private VBox sentMessagesContainer;
    @FXML
    private VBox receivedMessagesContainer;

    @FXML
    private void initialize() {
        sendButton.setDisable(true);
        HBox.setHgrow(messageField, Priority.ALWAYS);

        messageField.textProperty().addListener((obs, oldText, newText) -> {
            sendButton.setDisable(newText == null || newText.trim().isEmpty());
        });
    }

    @FXML
    private void onSend() {

    }

    @FXML
    private void onLogout() {
        ScreenManager.getInstance().showScene("intro");
    }

    public void setUser(User user) {
        this.currentUser = user;
        if (userLabel != null && user != null) {
            userLabel.setText(user.getUsername());
        }
    }
    

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}