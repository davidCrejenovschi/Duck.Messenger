package ui.graphical.controllers;
import entities.users.ducks.abstracts.Duck;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.StackPane;
import services.users.DuckService;

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
    private DuckService duckService;
    private FilteredList<Duck> filteredDucks;

    public void setDuckService(DuckService duckService) {
        this.duckService = duckService;
    }

    public void build() {
        loadDucks();
        setupComboBox();
    }

    private void loadDucks() {
        ObservableList<Duck> allDucks = FXCollections.observableArrayList(duckService.getAllUsers());
        filteredDucks = new FilteredList<>(allDucks, d -> true);
        duckTable.setItems(filteredDucks);
    }

    private void setupComboBox() {
        duckTypeComboBox.getItems().addAll("SWIMMER", "FLYER", "ALL");
        duckTypeComboBox.setValue("ALL");

        duckTypeComboBox.setOnAction(e -> {
            String selectedType = duckTypeComboBox.getValue();
            if (selectedType != null && !selectedType.equalsIgnoreCase("ALL")) {
                filteredDucks.setPredicate(d ->
                        d.getDuckType() != null &&
                                d.getDuckType().toString().equalsIgnoreCase(selectedType)
                );
            } else {
                filteredDucks.setPredicate(d -> true);
            }
        });
    }

    @FXML
    public void initialize() {

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

        colUsername.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUsername()));
        colPassword.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPassword()));
        colEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        colDuckType.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDuckType().toString()));
        colSpeed.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getSpeed()).asObject());
        colStamina.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getStamina()).asObject());
    }
}
