package ui.graphical.controllers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ui.graphical.ScreenManager;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class IntroController implements Initializable {

    @FXML
    private Button duckButton;
    @FXML
    private Button friendshipButton;
    @FXML
    private Button personButton;
    @FXML
    private ImageView duckImage;
    @FXML
    private ImageView friendshipImage;
    @FXML
    private ImageView personImage;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    @FXML
    private void onDuckButton() {
        ScreenManager.getInstance().showScene("duck");
    }

    @FXML
    private void onFriendshipButton() {
        ScreenManager.getInstance().showScene("friendship");
    }

    @FXML
    private void onPersonButton() {
        ScreenManager.getInstance().showScene("person");
    }

    @FXML
    private void onLogin() {

        ScreenManager.getInstance().showScene("chat");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image duckI = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/gui/images/duckIcon.png"))
        );
        duckImage.setImage(duckI);
        duckImage.setSmooth(true);

        Image friendshipI = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/gui/images/friendshipIcon.png"))
        );
        friendshipImage.setImage(friendshipI);
        friendshipImage.setSmooth(true);

        Image personI = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/gui/images/personIcon.png"))
        );
        personImage.setImage(personI);
        personImage.setSmooth(true);

    }

}
