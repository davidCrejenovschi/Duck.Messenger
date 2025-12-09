package ui.graphical.controllers;

import entities.pairs.Friendship;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import services.FriendshipService;
import ui.graphical.ScreenManager;
import java.util.Objects;
import java.util.Set;

public class FriendshipController {

    @FXML
    private TextField user1Field;
    @FXML
    private TextField user2Field;
    @FXML
    private TableView<Friendship> friendshipTable;
    @FXML
    private TableColumn<Friendship, Long> colUser1;
    @FXML
    private TableColumn<Friendship, Long> colUser2;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button backButton;
    @FXML
    private Button communitiesButton;
    @FXML
    private Button mostSociableCommunityButton;
    @FXML
    private TextArea smallScreen;

    private FriendshipService friendshipService;

    private final ObservableList<Friendship> model = FXCollections.observableArrayList();

    public void setService(FriendshipService service) {
        this.friendshipService = service;
        loadFriendships();
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
    }

    private void setupTable() {

        colUser1.setCellValueFactory(param ->
                new SimpleLongProperty(param.getValue().getLeft()).asObject());

        colUser2.setCellValueFactory(param ->
                new SimpleLongProperty(param.getValue().getRight()).asObject());

        colUser1.setStyle("-fx-alignment: CENTER;");
        colUser2.setStyle("-fx-alignment: CENTER;");

        friendshipTable.setItems(model);

        friendshipTable.widthProperty().addListener((obs, oldW, newW) -> {
            double colWidth = newW.doubleValue() / 2;
            colUser1.setPrefWidth(colWidth);
            colUser2.setPrefWidth(colWidth);
        });
    }

    private void loadFriendships() {
        if (friendshipService != null)
            model.setAll(friendshipService.getAllFriendships());
    }

    @FXML
    private void onAddFriendship() {
        String u1 = user1Field.getText().trim();
        String u2 = user2Field.getText().trim();

        if (u1.isEmpty() || u2.isEmpty()) {
            showError("Both user IDs are required.");
            return;
        }

        try {
            long id1 = Long.parseLong(u1);
            long id2 = Long.parseLong(u2);

            friendshipService.addFriendship(id1, id2);

            showInfo("Friendship added.");
            clearFields();
            loadFriendships();

        } catch (NumberFormatException e) {
            showError("IDs must be numbers.");
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    @FXML
    private void onRemoveFriendship() {
        String u1 = user1Field.getText().trim();
        String u2 = user2Field.getText().trim();

        if (u1.isEmpty() || u2.isEmpty()) {
            showError("Both user IDs are required.");
            return;
        }

        try {
            long id1 = Long.parseLong(u1);
            long id2 = Long.parseLong(u2);

            long fid;

            if (id1 > id2)
                fid = Long.parseLong(id1 + "0" + id2);
            else
                fid = Long.parseLong(id2 + "0" + id1);

            friendshipService.deleteFriendship(fid);

            showInfo("Friendship removed.");
            clearFields();
            loadFriendships();

        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    @FXML
    private void onCommunitiesButton() {
        try {
            int count = friendshipService.viewCommunities(friendshipService.getAllFriendships());
            smallScreen.setText("Number of communities: " + count);
        } catch (Exception e) {
            showError("Error retrieving communities: " + e.getMessage());
        }
    }

    @FXML
    private void onMostSociableCommunityButton() {
        try {
            Set<Long> mostSociable = friendshipService.viewMostSociableCommunity(friendshipService.getAllFriendships());

            if (mostSociable == null || mostSociable.isEmpty()) {
                smallScreen.setText("No communities found.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("The most developed community is formed by users with ids: ");
            for (Long id : mostSociable) {
                sb.append(id).append(" ");
            }
            smallScreen.setText(sb.toString().trim());
        } catch (Exception e) {
            showError("Error retrieving most sociable community: " + e.getMessage());
        }
    }

    private void clearFields() {
        user1Field.clear();
        user2Field.clear();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.showAndWait();
    }

    @FXML
    private void onBack() {
        ScreenManager.getInstance().showScene("intro");
    }

}
