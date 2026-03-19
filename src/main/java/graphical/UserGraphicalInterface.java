package graphical;
import graphical.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class UserGraphicalInterface extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        AppServiceManager appServiceManager = new AppServiceManager();

        loadLoginWindow(primaryStage, 0, appServiceManager);

        Stage secondStage = new Stage();
        loadLoginWindow(secondStage, 650, appServiceManager);
    }


    private void loadLoginWindow(Stage stage, int xOffset, AppServiceManager manager) throws IOException {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/loginView.fxml"));
        Parent root = loader.load();

        LoginController loginController = loader.getController();

        loginController.setAppServiceManager(manager, stage);

        Scene scene = new Scene(root);

        URL css = getClass().getResource("/gui/css/loginStyle.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}