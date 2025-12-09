package ui.graphical;
import javafx.application.Application;
import javafx.stage.Stage;
import repository.for_data_base.FriendshipDataBaseRepository;
import repository.for_data_base.PersonDataBaseRepository;
import repository.for_data_base.paginig.DuckDBPagingRepository;
import services.FriendshipService;
import services.users.DuckService;
import services.users.PersonService;
import ui.graphical.controllers.*;
import validators.FriendshipValidator;
import validators.users.DuckValidator;
import validators.users.PersonValidator;


public class UserGraphicalInterface extends Application {

    @Override
    public void start(Stage stage) {

        ScreenManager manager = ScreenManager.getInstance();
        manager.setStage(stage);

        manager.loadScene("intro", "/gui/views/introView.fxml");
        manager.loadScene("duck", "/gui/views/duckView.fxml");
        manager.loadScene("friendship", "/gui/views/friendshipView.fxml");
        manager.loadScene("person",  "/gui/views/personView.fxml");
        manager.loadScene("chat", "/gui/views/chatView.fxml");

        DuckController duck = (DuckController) manager.getController("duck");
        FriendshipController friendshipController = (FriendshipController) manager.getController("friendship");
        PersonController personController = (PersonController) manager.getController("person");

        DuckService duckService = createDuckService();
        PersonService personService = createPersonService();

        duck.setDuckService(duckService);
        friendshipController.setService(createFriendshipService());
        personController.setService(personService);

        duck.build();

        manager.showScene("intro");
        stage.setMaximized(true);

    }

    private DuckService createDuckService() {
        DuckDBPagingRepository duckDataBaseRepository = new DuckDBPagingRepository();
        DuckValidator duckValidator = new DuckValidator();
        return new DuckService(duckDataBaseRepository, duckValidator);
    }

    private FriendshipService createFriendshipService() {
        FriendshipDataBaseRepository  friendshipDataBaseRepository = new FriendshipDataBaseRepository();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        return new FriendshipService(friendshipDataBaseRepository, friendshipValidator);
    }

    private PersonService createPersonService() {
        PersonDataBaseRepository personDataBaseRepository = new PersonDataBaseRepository();
        PersonValidator  personValidator = new PersonValidator();
        return new PersonService(personDataBaseRepository, personValidator);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
