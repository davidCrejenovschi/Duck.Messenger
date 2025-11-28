package ui.graphical;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.for_data_base.DuckDataBaseRepository;
import services.users.DuckService;
import ui.graphical.controllers.DuckController;
import validators.users.DuckValidator;
import java.io.IOException;
import java.net.URL;

public class UserGraphicalInterface extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/duckView.fxml"));
        Parent root = loader.load();
        URL cssURL = getClass().getResource("/gui/css/duckStyle.css");
        if (cssURL != null) {
            root.getStylesheets().add(cssURL.toExternalForm());
        }
        DuckController controller = loader.getController();
        controller.setDuckService(createDuckService());
        controller.build();

        stage.setTitle("Duck Manager");

        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private DuckService createDuckService(){

        DuckDataBaseRepository duckDataBaseRepository = new DuckDataBaseRepository();
        DuckValidator duckValidator = new DuckValidator();
        return new DuckService(duckDataBaseRepository, duckValidator);
    }
}
