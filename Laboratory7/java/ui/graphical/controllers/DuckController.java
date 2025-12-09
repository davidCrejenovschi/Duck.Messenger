package ui.graphical.controllers;

import entities.dtos.users.ducks.DuckDTO;
import entities.dtos.users.ducks.DuckFilterDTO;
import entities.users.ducks.abstracts.Duck;
import entities.users.ducks.enums.DuckType;
import entities.utils.Page;
import entities.utils.Pageable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import services.users.DuckService;
import ui.graphical.ScreenManager;
import javafx.scene.control.TextField;

import java.awt.*;
import java.util.Objects;

public class DuckController {

    @FXML
    private TableView<Duck> duckTable;
    @FXML
    private TableColumn<Duck, String> colUsername;
    @FXML
    private TableColumn<Duck, String> colPassword;
    @FXML
    private TableColumn<Duck, String> colEmail;
    @FXML
    private TableColumn<Duck, String> colDuckType;
    @FXML
    private TableColumn<Duck, Double> colSpeed;
    @FXML
    private TableColumn<Duck, Double> colStamina;
    @FXML
    private StackPane tableContainer;
    @FXML
    private ComboBox<String> duckTypeComboBox;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button nextPageButton;
    @FXML
    private Button backButton;
    @FXML
    private TextField idField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField duckTypeField;
    @FXML
    private TextField speedField;
    @FXML
    private TextField staminaField;
    @FXML
    private Button addDuckButton;

    private DuckService duckService;

    private final ObservableList<Duck> pageList = FXCollections.observableArrayList();
    private FilteredList<Duck> filteredDucks;

    private int currentPage = 0;
    private final int pageSize = 5;

    private DuckType currentFilterType = null;
    private final DuckFilterDTO currentFilterDTO = new  DuckFilterDTO();

    public void setDuckService(DuckService duckService) {
        this.duckService = duckService;
    }

    public void build() {
        filteredDucks = new FilteredList<>(pageList, d -> true);
        duckTable.setItems(filteredDucks);
        loadFilteredDucksPage(0);
    }

    private void setupTable() {

        colUsername.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUsername()));
        colPassword.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPassword()));
        colEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        colDuckType.setCellValueFactory(cell -> {
            DuckType dt = cell.getValue().getDuckType();
            return new SimpleStringProperty(dt == null ? "" : dt.toString());
        });
        colSpeed.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getSpeed()).asObject());
        colStamina.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getStamina()).asObject());

        duckTable.prefWidthProperty().bind(tableContainer.widthProperty().multiply(0.6));
        duckTable.prefHeightProperty().bind(tableContainer.heightProperty().multiply(0.6));

        duckTable.widthProperty().addListener((obs, oldVal, newVal) -> {
            double w = newVal.doubleValue();
            colUsername.setPrefWidth(w * 0.18);
            colPassword.setPrefWidth(w * 0.18);
            colEmail.setPrefWidth(w * 0.20);
            colDuckType.setPrefWidth(w * 0.14);
            colSpeed.setPrefWidth(w * 0.15);
            colStamina.setPrefWidth(w * 0.15);
        });
    }

    private void setupComboBox() {

        duckTypeComboBox.getItems().addAll("SWIMMER", "FLYER", "ALL");
        duckTypeComboBox.setValue("ALL");

        duckTypeComboBox.setOnAction(e -> {
            String selected = duckTypeComboBox.getValue();
            if (selected != null && !selected.equalsIgnoreCase("ALL")) {
                try {
                    currentFilterType = DuckType.valueOf(selected.toUpperCase());
                } catch (IllegalArgumentException ex) {
                    currentFilterType = null;
                }
            } else {
                currentFilterType = null;
            }
            loadFilteredDucksPage(0);
        });
    }

    private void loadFilteredDucksPage(int page) {

        Pageable pageable = new Pageable(page, pageSize);
        try {

            currentFilterDTO.setDuckType(currentFilterType);
            Page<Duck> duckPage = duckService.getDucksOnPage(pageable, currentFilterDTO);
            ObservableList<Duck> ducksOnPage = FXCollections.observableArrayList(duckPage.elementsOnPage());

            if (ducksOnPage.isEmpty() && page > 0) {
                showNoMorePagesAlert("No more ducks for this filter.");
                return;
            }

            pageList.setAll(ducksOnPage);

            if (currentFilterType != null) {
                filteredDucks.setPredicate(d -> d.getDuckType() == currentFilterType);
            } else {
                filteredDucks.setPredicate(d -> true);
            }

            currentPage = page;

        } catch (Exception e) {
            showErrorAlert("Error fetching page: " + e.getMessage());
        }
    }

    @FXML
    private void onNextPage() {
        loadFilteredDucksPage(currentPage + 1);
    }

    @FXML
    private void onPreviousPage() {
        if (currentPage == 0) {
            showNoMorePagesAlert("Already on the first page.");
            return;
        }
        loadFilteredDucksPage(currentPage - 1);
    }

    private void refreshCurrentPage() {
        if (duckService == null) return;

        int pageToTry = currentPage;
        while (pageToTry > 0) {
            Pageable pageable = new Pageable(pageToTry, pageSize);
            try {
                currentFilterDTO.setDuckType(currentFilterType);
                Page<Duck> duckPage = duckService.getDucksOnPage(pageable, currentFilterDTO);
                if (!duckPage.elementsOnPage().isEmpty()) {
                    loadFilteredDucksPage(pageToTry);
                    return;
                }
                pageToTry--;
            } catch (Exception e) {
                showErrorAlert("Error refreshing page: " + e.getMessage());
                return;
            }
        }
        loadFilteredDucksPage(0);
    }

    @FXML
    public void initialize() {
        Image backI = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/gui/images/backIcon.png"))
        );
        ImageView backImageView = new ImageView(backI);
        backImageView.setFitWidth(40);
        backImageView.setFitHeight(40);
        backImageView.setPreserveRatio(true);
        backButton.setGraphic(backImageView);

        setupTable();
        setupComboBox();
    }

    @FXML
    private void onBack() {
        ScreenManager.getInstance().showScene("intro");
    }

    @FXML
    private void onAddDuck() {
        try {
            String idText = idField.getText();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String email = emailField.getText().trim();
            String typeText = duckTypeField.getText().trim();
            String speedText = speedField.getText().trim();
            String staminaText = staminaField.getText().trim();

            if (idText.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() ||
                    typeText.isEmpty() || speedText.isEmpty() || staminaText.isEmpty()) {
                showErrorAlert("All fields must be filled!");
                return;
            }

            DuckType duckType;
            try {
                duckType = DuckType.valueOf(typeText.toUpperCase());
            } catch (IllegalArgumentException e) {
                showErrorAlert("Invalid Duck Type! Use a valid type like SWIMMER or FLYER.");
                return;
            }

            double speed, stamina;
            try {
                speed = Double.parseDouble(speedText);
                stamina = Double.parseDouble(staminaText);
            } catch (NumberFormatException e) {
                showErrorAlert("Speed and Stamina must be numbers.");
                return;
            }

            long id;
            try {
                id = Long.parseLong(idText);
            } catch (NumberFormatException e) {
                showErrorAlert("Id must be an integer.");
                return;
            }

            DuckDTO duckDTO = new DuckDTO(id, username, password, email, duckType, speed, stamina);

            duckService.addUser(duckDTO);

            idField.clear();
            usernameField.clear();
            passwordField.clear();
            emailField.clear();
            duckTypeField.clear();
            speedField.clear();
            staminaField.clear();

            refreshCurrentPage();

        } catch (Exception e) {
            showErrorAlert("Error adding duck: " + e.getMessage());
        }
    }

    @FXML
    private void onDeleteDuck() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            showErrorAlert("Please enter the ID of the duck to delete.");
            return;
        }

        long id;
        try {
            id = Long.parseLong(idText);
        } catch (NumberFormatException e) {
            showErrorAlert("ID must be a valid number.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete the duck with ID " + id + "?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    duckService.deleteUser(id);
                    showInfoAlert("Duck deleted successfully.");
                    idField.clear();
                    loadFilteredDucksPage(currentPage);
                } catch (Exception e) {
                    showErrorAlert("Error deleting duck: " + e.getMessage());
                }
            }
        });
    }

    private void showNoMorePagesAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pagination Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
