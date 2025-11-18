import repository.for_data_base.DuckDataBaseRepository;
import repository.for_data_base.PersonDataBaseRepository;
import repository.for_files.*;
import services.events.RaceEventService;
import services.FlockService;
import services.FriendshipService;
import services.users.DuckService;
import services.users.PersonService;
import ui.UserInterface;
import validators.*;
import validators.events.RaceEventValidator;
import validators.users.DuckValidator;
import validators.users.PersonValidator;

void main() {

    PersonDataBaseRepository personDataBaseRepository = new PersonDataBaseRepository();
    DuckDataBaseRepository duckDataBaseRepository = new DuckDataBaseRepository();
    FriendshipFileRepository friendshipRepository = new FriendshipFileRepository("D:\\Lab 3\\src\\files\\friendships.txt");
    FlockFileRepository flockRepository = new FlockFileRepository("D:\\Lab 3\\src\\files\\flocks.txt");
    RaceEventFileRepository raceEventRepository = new RaceEventFileRepository("D:\\Lab 3\\src\\files\\raceEvents");

    PersonValidator personValidator = new PersonValidator();
    DuckValidator duckValidator = new DuckValidator();
    FriendshipValidator friendshipValidator = new FriendshipValidator();
    FlockValidator flockValidator = new FlockValidator();
    RaceEventValidator raceEventValidator = new RaceEventValidator();

    PersonService personService = new PersonService(personDataBaseRepository, personValidator);
    DuckService duckService = new DuckService(duckDataBaseRepository, duckValidator);
    FriendshipService friendshipService = new FriendshipService(friendshipRepository, friendshipValidator);
    FlockService flockService = new FlockService(flockRepository, flockValidator);
    RaceEventService raceEventService = new RaceEventService(raceEventRepository, raceEventValidator);

    UserInterface userInterface = new UserInterface(duckService, personService, friendshipService, flockService,  raceEventService);
    userInterface.start();

}