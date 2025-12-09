package ui.graphical;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class ScreenManager {

    private static final ScreenManager instance = new ScreenManager();
    public static ScreenManager getInstance() { return instance; }
    private Stage stage;
    private final Map<String, Scene> scenes = new HashMap<>();
    private final Map<String, Object> controllers = new HashMap<>();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadScene(String name, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            String cssPath = "/gui/css/" + name + "Style.css";
            URL css = getClass().getResource(cssPath);

            if (css != null) {
                scene.getStylesheets().add(css.toExternalForm());
            } else {
                System.out.println("⚠️ CSS not found: " + cssPath);
            }

            scenes.put(name, scene);
            controllers.put(name, loader.getController());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showScene(String key) {
        Scene scene = scenes.get(key);
        if (scene == null) throw new RuntimeException("Scene not found: " + key);

        stage.setScene(scene);
        stage.setResizable(true);

        Rectangle2D vb = Screen.getPrimary().getVisualBounds();
        stage.setX(vb.getMinX());
        stage.setY(vb.getMinY());
        stage.setWidth(vb.getWidth());
        stage.setHeight(vb.getHeight());

        stage.setTitle(capitalize(key) + " Manager");

        stage.show();
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return "";
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public Object getController(String key) {
        return controllers.get(key);
    }
}
