package ui.graphical.controllers;
import entities.dtos.users.PersonDTO;
import entities.users.persons.Person;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import services.users.PersonService;
import ui.graphical.ScreenManager;

import java.time.LocalDate;
import java.util.Objects;

public class PersonController {

    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, Long> colId;
    @FXML
    private TableColumn<Person, String> colUsername;
    @FXML
    private TableColumn<Person, String> colPassword;
    @FXML
    private TableColumn<Person, String> colEmail;
    @FXML
    private TableColumn<Person, String> colFirstName;
    @FXML
    private TableColumn<Person, String> colLastName;
    @FXML
    private TableColumn<Person, LocalDate> colBirthDate;
    @FXML
    private TableColumn<Person, String> colOccupation;
    @FXML
    private TableColumn<Person, Integer> colEmpathy;
    @FXML
    private TextField idField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private TextField occupationField;
    @FXML
    private TextField empathyField;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button backButton;

    private PersonService personService;
    private final ObservableList<Person> persons = FXCollections.observableArrayList();

    public void setService(PersonService service) {
        this.personService = service;
        loadPersons();
    }

    @FXML
    public void initialize() {
        setupTable();
        personTable.setItems(persons);
        personTable.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double tableWidth = newWidth.doubleValue();
            int numColumns = personTable.getColumns().size();
            double colWidth = tableWidth / numColumns;

            for (TableColumn<Person, ?> col : personTable.getColumns()) {
                col.setPrefWidth(colWidth);
            }
        });
        Image backI = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/gui/images/backIcon.png"))
        );
        ImageView backImageView = new ImageView(backI);
        backImageView.setFitWidth(40);
        backImageView.setFitHeight(40);
        backImageView.setPreserveRatio(true);
        backButton.setGraphic(backImageView);
    }

    private void setupTable() {
        colId.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getId()));
        colUsername.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUsername()));
        colPassword.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPassword()));
        colEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        colFirstName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFirst_name()));
        colLastName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLast_name()));
        colBirthDate.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getBirth_date()));
        colOccupation.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getOccupation()));
        colEmpathy.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getEmpathyLevel()).asObject());

        colId.setStyle("-fx-alignment: CENTER;");
        colEmpathy.setStyle("-fx-alignment: CENTER;");
    }

    public void loadPersons() {
        if (personService != null) {
            persons.setAll(personService.getAllUsers());
        }
    }

    @FXML
    private void onAddPerson() {
        try {
            String idText = idField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String email = emailField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            LocalDate birthDate = birthDatePicker.getValue();
            String occupation = occupationField.getText().trim();
            String empathyText = empathyField.getText().trim();

            if (idText.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty()
                    || firstName.isEmpty() || lastName.isEmpty() || birthDate == null
                    || occupation.isEmpty() || empathyText.isEmpty()) {
                showErrorAlert("All fields must be filled!");
                return;
            }

            long id;
            try {
                id = Long.parseLong(idText);
            } catch (NumberFormatException e) {
                showErrorAlert("ID must be a valid integer.");
                return;
            }

            int empathy;
            try {
                empathy = Integer.parseInt(empathyText);
            } catch (NumberFormatException e) {
                showErrorAlert("Empathy level must be a number.");
                return;
            }
            PersonDTO personDTO = new PersonDTO(id, username, password, email, firstName, lastName, occupation, empathy, birthDate);

            personService.addUser(personDTO);

            clearForm();
            loadPersons();

        } catch (Exception e) {
            showErrorAlert("Error adding person: " + e.getMessage());
        }
    }

    @FXML
    private void onDeletePerson() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            showErrorAlert("Please enter the ID of the person to delete.");
            return;
        }

        long id;
        try {
            id = Long.parseLong(idText);
        } catch (NumberFormatException e) {
            showErrorAlert("ID must be a valid number.");
            return;
        }

        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete the person with ID " + id + "?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    personService.deleteUser(id);
                    showInfoAlert("Person deleted successfully.");
                    idField.clear();
                    loadPersons();
                } catch (Exception e) {
                    showErrorAlert("Error deleting person: " + e.getMessage());
                }
            }
        });
    }

    private void showErrorAlert(String msg) {
        Alert alert = new Alert(AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    private void showInfoAlert(String msg) {
        Alert alert = new Alert(AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }

    private void clearForm() {
        idField.clear();
        usernameField.clear();
        passwordField.clear();
        emailField.clear();
        firstNameField.clear();
        lastNameField.clear();
        birthDatePicker.setValue(null);
        occupationField.clear();
        empathyField.clear();
    }

    @FXML
    private void onBack() {
        ScreenManager.getInstance().showScene("intro");
    }
}
