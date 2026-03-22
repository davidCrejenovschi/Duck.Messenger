package graphical.controllers;
import entities.dtos.DuckDTO;
import entities.dtos.PersonDTO;
import exceptions.ValidationException;
import graphical.AppServiceManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.DuckService;
import services.PersonService;

import java.io.IOException;
import java.net.URL;

public class RegisterController {

    @FXML private RadioButton rbDuck;
    @FXML private RadioButton rbPerson;
    @FXML private VBox fieldsContainer;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button registerButton;
    @FXML private Hyperlink loginLink;

    Stage currentStage;
    DuckService duckService;
    PersonService personService;
    AppServiceManager appServiceManager;

    @FXML
    private void initialize() {
        rbDuck.setOnAction(event -> updateEntityFields());
        rbPerson.setOnAction(event -> updateEntityFields());
        updateEntityFields();
    }

    @FXML
    private void updateEntityFields() {
        fieldsContainer.getChildren().clear();
        fieldsContainer.getChildren().addAll(usernameField, emailField, passwordField);

        if (rbDuck.isSelected()) {
            TextField duckType = new TextField();
            duckType.setPromptText("Duck Type");
            duckType.getStyleClass().add("text-field-modern");

            TextField speed = new TextField();
            speed.setPromptText("Speed");
            speed.getStyleClass().add("text-field-modern");

            TextField stamina = new TextField();
            stamina.setPromptText("Stamina");
            stamina.getStyleClass().add("text-field-modern");

            fieldsContainer.getChildren().addAll(duckType, speed, stamina);
        } else {
            TextField firstName = new TextField();
            firstName.setPromptText("First Name");
            firstName.getStyleClass().add("text-field-modern");

            TextField lastName = new TextField();
            lastName.setPromptText("Last Name");
            lastName.getStyleClass().add("text-field-modern");

            DatePicker birthDate = new DatePicker();
            birthDate.setPromptText("Birth Date");
            birthDate.setMaxWidth(Double.MAX_VALUE);

            TextField occupation = new TextField();
            occupation.setPromptText("Occupation");
            occupation.getStyleClass().add("text-field-modern");

            TextField empathy = new TextField();
            empathy.setPromptText("Empathy Level");
            empathy.getStyleClass().add("text-field-modern");

            fieldsContainer.getChildren().addAll(firstName, lastName, birthDate, occupation, empathy);
        }
    }

    private void clearCommonFields() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
    }

    @FXML
    private void onRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            if (rbDuck.isSelected()) {
                var children = fieldsContainer.getChildren();

                String duckType = ((TextField) children.get(3)).getText();
                String speedText = ((TextField) children.get(4)).getText();
                String staminaText = ((TextField) children.get(5)).getText();

                double speed;
                double stamina;

                try {
                    speed = Double.parseDouble(speedText);
                    stamina = Double.parseDouble(staminaText);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "Speed and Stamina must be valid numbers!");
                    return;
                }

                DuckDTO duckDTO = new DuckDTO();
                duckDTO.setDuckType(duckType);
                duckDTO.setUsername(username);
                duckDTO.setEmail(email);
                duckDTO.setPassword(password);
                duckDTO.setSpeed(speed);
                duckDTO.setStamina(stamina);

                duckService.addDuck(duckDTO);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Successfully Registered!");

                usernameField.clear();
                passwordField.clear();
                emailField.clear();
                ((TextField) children.get(3)).clear();
                ((TextField) children.get(4)).clear();
                ((TextField) children.get(5)).clear();

            } else {
                var children = fieldsContainer.getChildren();

                String firstName = ((TextField) children.get(3)).getText();
                String lastName = ((TextField) children.get(4)).getText();
                java.time.LocalDate birthDate = ((DatePicker) children.get(5)).getValue();
                String occupation = ((TextField) children.get(6)).getText();
                String empathyText = ((TextField) children.get(7)).getText();

                if (birthDate == null) {
                    showAlert(Alert.AlertType.ERROR,"Validation Error", "Birth date is required!");
                    return;
                }

                int empathy;
                try {
                    empathy = Integer.parseInt(empathyText);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR,"Validation Error", "Empathy level must be a valid number!");
                    return;
                }

                PersonDTO personDTO = new PersonDTO();
                personDTO.setFirstName(firstName);
                personDTO.setLastName(lastName);
                personDTO.setBirthDate(birthDate);
                personDTO.setOccupation(occupation);
                personDTO.setUsername(username);
                personDTO.setEmail(email);
                personDTO.setPassword(password);
                personDTO.setEmpathyLevel(empathy);

                personService.addPerson(personDTO);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Person added!");

                usernameField.clear();
                emailField.clear();
                passwordField.clear();
                ((TextField) children.get(3)).clear();
                ((TextField) children.get(4)).clear();
                ((DatePicker) children.get(5)).setValue(null);
                ((TextField) children.get(6)).clear();
                ((TextField) children.get(7)).clear();
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void onLoginLink() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/loginView.fxml"));
        Parent root = loader.load();

        LoginController loginController = loader.getController();
        Stage loginStage = new Stage();
        loginController.setAppServiceManager(this.appServiceManager, loginStage);

        Scene scene = new Scene(root);

        URL css = getClass().getResource("/gui/css/loginStyle.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        loginStage.setScene(scene);
        loginStage.setTitle("Login");
        loginStage.show();

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

    public  void setAppServiceManager(AppServiceManager appServiceManager, Stage registerStage) {
        currentStage = registerStage;
        this.appServiceManager = appServiceManager;
        setDuckService(appServiceManager.getDuckService());
        setPersonService(appServiceManager.getPersonService());
    }
}