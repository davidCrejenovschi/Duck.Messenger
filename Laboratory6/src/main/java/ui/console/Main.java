package ui.console;
import repository.for_data_base.*;
import repository.for_files.*;
import services.events.RaceEventService;
import services.FlockService;
import services.FriendshipService;
import services.users.DuckService;
import services.users.PersonService;
import validators.*;
import validators.events.RaceEventValidator;
import validators.users.DuckValidator;
import validators.users.PersonValidator;

class Main {

    static void main() {

        PersonDataBaseRepository personDataBaseRepository = new PersonDataBaseRepository();
        DuckDataBaseRepository duckDataBaseRepository = new DuckDataBaseRepository();
        FriendshipDataBaseRepository friendshipDataBaseRepository = new FriendshipDataBaseRepository();
        FlockDataBaseRepository flockDataBaseRepository = new FlockDataBaseRepository();
        RaceEventDataBaseRepository raceEventDataBaseRepository = new RaceEventDataBaseRepository();

        PersonValidator personValidator = new PersonValidator();
        DuckValidator duckValidator = new DuckValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        FlockValidator flockValidator = new FlockValidator();
        RaceEventValidator raceEventValidator = new RaceEventValidator();

        PersonService personService = new PersonService(personDataBaseRepository, personValidator);
        DuckService duckService = new DuckService(duckDataBaseRepository, duckValidator);
        FriendshipService friendshipService = new FriendshipService(friendshipDataBaseRepository, friendshipValidator);
        FlockService flockService = new FlockService(flockDataBaseRepository, flockValidator);
        RaceEventService raceEventService = new RaceEventService(raceEventDataBaseRepository, raceEventValidator);

        UserInterface userInterface = new UserInterface(duckService, personService, friendshipService, flockService, raceEventService);
        userInterface.start();
    }
}