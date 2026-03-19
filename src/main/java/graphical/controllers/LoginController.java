package graphical.controllers;

import entities.users.AbstractUser;
import graphical.AppServiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.DuckService;
import services.PersonService;

import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Hyperlink registerNowLink;

    DuckService duckService;
    PersonService personService;
    AppServiceManager appServiceManager;
    Stage currentStage;

    @FXML
    private void initialize() {

    }

    @FXML
    private void onLogin(ActionEvent event) throws IOException {

        String username = usernameField.getText();
        String password = passwordField.getText();

        AbstractUser user = duckService.findDuckByUsername(username);
        if (user == null) {
            user = personService.findPersonByUsername(username);
        }

        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("The username does not have an account associated.");
            alert.showAndWait();
            return;
        }

        if (!user.getPassword().equals(password)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("The password is incorrect.");
            alert.showAndWait();
            return;
        }


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/chatView.fxml"));
        Parent root = loader.load();

        ChatController chatController = loader.getController();
        Stage chatStage = new Stage();
        chatController.setAppServiceManager(appServiceManager, chatStage);
        chatController.setCurrentUser(user);

        Scene scene = new Scene(root);

        URL css = getClass().getResource("/gui/css/chatStyle.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        chatStage.setScene(scene);
        chatStage.setTitle("Chat");
        chatStage.show();

        if (currentStage != null) {
            currentStage.close();
        }

    }

    @FXML
    private void onCreateAccount(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/registerView.fxml"));
        Parent root = loader.load();

        RegisterController registerController = loader.getController();
        Stage registerStage = new Stage();
        registerController.setAppServiceManager(this.appServiceManager, registerStage);

        Scene scene = new Scene(root);


        URL css = getClass().getResource("/gui/css/registerStyle.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        registerStage.setScene(scene);
        registerStage.setTitle("Register");
        registerStage.show();

        if (currentStage != null) {
            currentStage.close();
        }

    }

    public void setDuckService(DuckService duckService) {
        this.duckService = duckService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public void setAppServiceManager(AppServiceManager appServiceManager, Stage stage) {

        currentStage = stage;
        this.appServiceManager = appServiceManager;
        setDuckService(appServiceManager.getDuckService());
        setPersonService(appServiceManager.getPersonService());
    }
}
